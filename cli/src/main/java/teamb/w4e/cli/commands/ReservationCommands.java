package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CartElement;
import teamb.w4e.cli.model.CliActivity;
import teamb.w4e.cli.model.CliReservation;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ShellComponent
public class ReservationCommands {
    public static final String CATALOG_URI = "/catalog/activities";
    public static final String BASE_URI = "/customers";

    private final RestTemplate restTemplate;
    private final CliContext cliContext;

    @Autowired
    public ReservationCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }


    @ShellMethod("Show cart content of customer (showcart CUSTOMER_NAME)")
    public Set<CartElement> showCart(String name) {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(getUriForCustomer(name), CartElement[].class)
                .getBody())).collect(toSet());
    }

    @ShellMethod("Add cookie to cart of customer (add-to-cart CUSTOMER_NAME ACTIVITY_NAME DATE(dd-MM HH:mm))")
    public CartElement addToCart(String customerName, String activityName, String day, String hour) {
        ResponseEntity<CliActivity> activityResponse = restTemplate.getForEntity(getUriForActivity(activityName), CliActivity.class);
        CartElement element = new CartElement((Objects.requireNonNull(activityResponse.getBody())), day + " " + hour);
        return restTemplate.postForObject(getUriForCustomer(customerName), element, CartElement.class);
    }

//    @ShellMethod("Validate cart of customer (validate CUSTOMER_NAME)")
//    public CliOrder validateCart(String name) {
//        return restTemplate.postForObject(getUriForCustomer(name) + "/validate", null, CliOrder.class);
//    }

    @ShellMethod("Reserve an activity (reserve CUSTOMER_NAME)")
    public CliReservation reserve(String customerName) {
        return restTemplate.postForObject(getUriForCustomer(customerName) + "/reserve", null, CliReservation.class);
    }

    private String getUriForActivity(String name) {
        return CATALOG_URI + "/" + cliContext.getActivities().get(name).getId();
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/cart";
    }
}
