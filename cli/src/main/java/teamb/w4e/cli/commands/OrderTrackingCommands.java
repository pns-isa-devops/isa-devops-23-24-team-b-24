package teamb.w4e.cli.commands;

import teamb.w4e.cli.model.CliOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ShellComponent
public class OrderTrackingCommands {

    public static final String BASE_URI = "/orders";

    private final RestTemplate restTemplate;

    @Autowired
    public OrderTrackingCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Show all orders)")
    public Set<CliOrder> orders() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliOrder[].class)
                .getBody())).collect(toSet());
    }

    @ShellMethod("Show order status by id (order-status ORDER_ID)")
    public String orderStatus(Long id) {
        return restTemplate.getForObject(BASE_URI+ "/" + id + "/status", String.class);
    }

}
