package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliActivity;
import teamb.w4e.cli.model.CliGroup;
import teamb.w4e.cli.model.CliReservation;
import teamb.w4e.cli.model.ReservationType;
import teamb.w4e.cli.model.cart.CartElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ShellComponent
public class CartCommands {
    public static final String CATALOG_URI = "/catalog/activities";
    public static final String BASE_URI = "/customers";

    private final RestTemplate restTemplate;
    private final CliContext cliContext;

    @Autowired
    public CartCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }


    @ShellMethod("Show cart content of customer (showcart CUSTOMER_NAME)")
    public Set<CartElement> showCart(String name) {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(getUriForCustomer(name), CartElement[].class)
                .getBody())).collect(toSet());
    }

    @ShellMethod("Add activity to cart of customer (add-to-cart CUSTOMER_NAME ACTIVITY_NAME)")
    public CartElement addToCart(
            String customerName,
            String activityName,
            @ShellOption(value = "-g", defaultValue = "false") boolean groupActivity,
            @ShellOption(value = "-t", defaultValue = "false") boolean timeSlot,
            @ShellOption(value = "--day") String day,
            @ShellOption(value = "--hour") String hour,
            @ShellOption(value = "-s", defaultValue = "false") boolean skiPass,
            @ShellOption(value = "--type") String skiPassType,
            @ShellOption(value = "--duration", defaultValue ="0") int duration) {
        if (groupActivity && timeSlot) {
            throw new IllegalArgumentException("Options -g and -t cannot be combined.");
        }
        if (groupActivity) {
            ResponseEntity<CliActivity> activityResponse = restTemplate.getForEntity(getUriForActivity(activityName), CliActivity.class);
            ResponseEntity<CliGroup> groupResponse = restTemplate.getForEntity(getUriForGroup(customerName), CliGroup.class);
            return restTemplate.postForObject(getUriForCustomer(customerName), new CartElement(ReservationType.GROUP, Objects.requireNonNull(activityResponse.getBody()), groupResponse.getBody()), CartElement.class);

        } else if (timeSlot) {
            if (day.isEmpty() || hour.isEmpty()) {
                throw new IllegalArgumentException("Option -h requires specifying both --day and --hour.");
            }
            ResponseEntity<CliActivity> activityResponse = restTemplate.getForEntity(getUriForActivity(activityName), CliActivity.class);
            return restTemplate.postForObject(getUriForCustomer(customerName), new CartElement(ReservationType.TIME_SLOT, Objects.requireNonNull(activityResponse.getBody()), day + " " + hour), CartElement.class);

        } else if (skiPass) {
            if (skiPassType.isEmpty() || duration == 0) {
                throw new IllegalArgumentException("Option -s requires specifying both --type and --duration.");
            }
            ResponseEntity<CliActivity> activityResponse = restTemplate.getForEntity(getUriForActivity(activityName), CliActivity.class);
            CartElement cartElement = new CartElement(ReservationType.SKI_PASS, Objects.requireNonNull(activityResponse.getBody()), skiPassType, duration);
            System.out.println(cartElement);
            return restTemplate.postForObject(getUriForCustomer(customerName), cartElement, CartElement.class);
        }
        throw new IllegalArgumentException("Either -g or -t must be specified.");
    }

    @ShellMethod("Reserve an activity (reserve CUSTOMER_NAME)")
    public Set<CliReservation> reserve(String customerName) {
        Set<CliReservation> reservations = new HashSet<>();
        for (CartElement element : showCart(customerName)) {
            reservations.add(restTemplate.postForObject(getUriForCustomer(customerName) + "/reservation", element, CliReservation.class));
        }
        return reservations;
    }

    private String getUriForActivity(String name) {
        return CATALOG_URI + "/" + cliContext.getActivities().get(name).getId();
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/cart";
    }

    private String getUriForGroup(String name) {
        return BASE_URI + "/groups/" + cliContext.getCustomers().get(name).getId() + "/group";
    }
}
