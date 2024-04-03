package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.*;
import teamb.w4e.cli.model.cart.CartElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ShellComponent
public class CartCommands {
    public static final String BASE_URI = "/customers";
    public static final String CATALOG_URI = "/catalog";
    private final RestTemplate restTemplate;
    private final CliContext cliContext;

    @Autowired
    public CartCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }


    @ShellMethod("Show cart content of customer (show-cart CUSTOMER_NAME)")
    public Set<CartElement> showCart(String name) {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(getUriForCustomer(name), CartElement[].class)
                .getBody())).collect(toSet());
    }

    @ShellMethod("Show advantage content of customer (show-advantages-cart CUSTOMER_NAME)")
    public Set<CliAdvantage> showAdvantagesCart(String name) {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(getUriForCustomer(name) + "/advantages", CliAdvantage[].class)
                .getBody())).collect(toSet());
    }

    @ShellMethod("Add activity to cart of customer (add-activity-to-cart CUSTOMER_NAME ACTIVITY_NAME)")
    public CartElement addActivityToCart(
            String customerName,
            String leisureName,
            @ShellOption(value = "-g", defaultValue = "false") boolean group,
            @ShellOption(value = "-t", defaultValue = "false") boolean timeSlot,
            @ShellOption(value = "-p", defaultValue = "false") boolean skiPass,
            @ShellOption(value = "--day", defaultValue = "") String day,
            @ShellOption(value = "--hour", defaultValue = "") String hour,
            @ShellOption(value = "--ski", defaultValue = "") String skiPassType,
            @ShellOption(value = "--duration", defaultValue = "0") int duration) {
        if (group && timeSlot || group && skiPass || timeSlot && skiPass) {
            throw new IllegalArgumentException("Options -g, -t and -p cannot be combined.");
        }
        if (group) {
            ResponseEntity<CliLeisure> activityResponse = restTemplate.getForEntity(getUriForActivity(leisureName), CliLeisure.class);
            ResponseEntity<CliGroup> groupResponse = restTemplate.getForEntity(getUriForGroup(customerName), CliGroup.class);
            CartElement groupElement = new CartElement(ReservationType.GROUP, Objects.requireNonNull(activityResponse.getBody()), groupResponse.getBody());
            return restTemplate.postForObject(getUriForCustomer(customerName), groupElement, CartElement.class);
        }
        if (timeSlot) {
            if (day.isEmpty() || hour.isEmpty()) {
                throw new IllegalArgumentException("Option -t requires specifying both day and hour.");
            }
            ResponseEntity<CliLeisure> activityResponse = restTemplate.getForEntity(getUriForActivity(leisureName), CliLeisure.class);
            CartElement timeSlotElement = new CartElement(ReservationType.TIME_SLOT, Objects.requireNonNull(activityResponse.getBody()), day + " " + hour);
            return restTemplate.postForObject(getUriForCustomer(customerName), timeSlotElement, CartElement.class);
        }
        if (skiPass) {
            if (skiPassType.isEmpty() || duration == 0) {
                throw new IllegalArgumentException("Option -p requires specifying both type and duration.");
            }
            ResponseEntity<CliLeisure> activityResponse = restTemplate.getForEntity(getUriForActivity(leisureName), CliLeisure.class);
            CartElement skiPassElement = new CartElement(ReservationType.SKI_PASS, Objects.requireNonNull(activityResponse.getBody()), skiPassType, duration);
            return restTemplate.postForObject(getUriForCustomer(customerName), skiPassElement, CartElement.class);
        }
        throw new IllegalArgumentException("Invalid combination of options.");
    }

    @ShellMethod("Add advantage to cart of customer (add-advantage-to-cart CUSTOMER_NAME ADVANTAGE_NAME)")
    public CliAdvantage addAdvantageToCart(String customerName, String advantageName) {
        ResponseEntity<CliAdvantage> advantageResponse = restTemplate.getForEntity(getUriForAdvantage(advantageName), CliAdvantage.class);
        return restTemplate.postForObject(getUriForCustomer(customerName) + "/advantages", Objects.requireNonNull(advantageResponse).getBody(), CliAdvantage.class);
    }


    @ShellMethod("Add service to cart of customer (add-service-to-cart CUSTOMER_NAME LEISURE_NAME)")
    public CartElement addServiceToCart(String customerName, String leisureName) {
        ResponseEntity<CliLeisure> serviceResponse = restTemplate.getForEntity(getUriForService(leisureName), CliLeisure.class);
        CartElement serviceElement = new CartElement(ReservationType.SERVICE, Objects.requireNonNull(serviceResponse.getBody()));
        return restTemplate.postForObject(getUriForCustomer(customerName), serviceElement, CartElement.class);
    }


    @ShellMethod("Reserve an activity (reserve CUSTOMER_NAME)")
    public Set<CliReservation> reserve(String customerName) {
        Set<CliReservation> reservations = new HashSet<>();
        for (CartElement element : showCart(customerName)) {
            if (element.getType().equals(ReservationType.SERVICE)) {
                continue;
            }
            reservations.add(restTemplate.postForObject(getUriForCustomer(customerName) + "/reservations", element, CliReservation.class));
        }
        return reservations;
    }

    @ShellMethod("Use a service (use-service CUSTOMER_NAME SERVICE_NAME)")
    public CliTransaction useService(String customerName, String serviceName) {
        if (showCart(customerName).isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }
        if (showCart(customerName).stream().noneMatch(element -> element.getLeisure().getName().equals(serviceName))) {
            throw new IllegalArgumentException("Service not found in cart.");
        }
        return restTemplate.postForObject(getUriForCustomer(customerName) + "/services", showCart(customerName).stream()
                .filter(element -> element.getLeisure().getName().equals(serviceName))
                .findFirst().orElseThrow(), CliTransaction.class);
    }

    private String getUriForAdvantage(String name) {
        return CATALOG_URI + "/advantages/" + cliContext.getAdvantages().get(name).getId();
    }

    private String getUriForActivity(String name) {
        return CATALOG_URI + "/activities/" + cliContext.getLeisure().get(name).getId();
    }

    private String getUriForService(String name) {
        return CATALOG_URI + "/services/" + cliContext.getLeisure().get(name).getId();
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/cart";
    }

    private String getUriForGroup(String name) {
        return BASE_URI + "/groups/" + cliContext.getCustomers().get(name).getId();
    }
}
