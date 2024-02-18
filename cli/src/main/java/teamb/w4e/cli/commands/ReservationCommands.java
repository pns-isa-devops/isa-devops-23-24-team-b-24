package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CartElement;
import teamb.w4e.cli.model.CliActivity;

@ShellComponent
public class ReservationCommands {
    public static final String CATALOG_URI = "/catalog/activities";
    public static final String BASE_URI = "/customers/reservations";

    private final RestTemplate restTemplate;
    private final CliContext cliContext;

    @Autowired
    public ReservationCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Reserve an activity (reserve-activity CUSTOMER_NAME ACTIVITY_NAME )")
    public CartElement reserveActivity(String customerName, String activityName) {
        ResponseEntity<CliActivity> activityResponse = restTemplate.getForEntity(getUriForActivity(activityName), CliActivity.class);
        if (activityResponse.getStatusCode() == HttpStatus.OK) {
            return restTemplate.postForEntity(getUriForCustomer(customerName), new CartElement(activityResponse.getBody()), CartElement.class).getBody();
        }
        return null;
    }

    private String getUriForActivity(String name) {
        return CATALOG_URI + cliContext.getActivities().get(name).getId();
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId();
    }
}
