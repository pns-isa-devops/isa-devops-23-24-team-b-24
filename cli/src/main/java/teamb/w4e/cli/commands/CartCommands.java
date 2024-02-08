package teamb.w4e.cli.commands;

import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CartElement;
import teamb.w4e.cli.model.CliOrder;
import teamb.w4e.cli.model.CookieEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ShellComponent
public class CartCommands {

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
        // Spring-shell is catching exception (could be the case if name is not from a valid customer
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(getUriForCustomer(name), CartElement[].class)
                .getBody())).collect(toSet());
    }

    @ShellMethod("Add cookie to cart of customer (add-to-cart CUSTOMER_NAME COOKIE_NAME QUANTITY)")
    public CartElement addToCart(String name, CookieEnum cookie, int quantity) {
        // Spring-shell is catching exception (could be the case if name is not from a valid customer)
        return restTemplate.postForObject(getUriForCustomer(name), new CartElement(cookie, quantity), CartElement.class);
    }

    @ShellMethod("Remove cookie from cart of customer (remove-from-cart CUSTOMER_NAME COOKIE_NAME QUANTITY)")
    public CartElement removeFromCart(String name, CookieEnum cookie, int quantity) {
        return addToCart(name, cookie, -quantity);
    }

    @ShellMethod("Validate cart of customer (validate CUSTOMER_NAME)")
    public CliOrder validateCart(String name) {
        return restTemplate.postForObject(getUriForCustomer(name) + "/validate", null, CliOrder.class);
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/cart";
    }

}
