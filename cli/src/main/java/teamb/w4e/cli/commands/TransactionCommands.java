package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliTransaction;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class TransactionCommands {

    public static final String BASE_URI = "/customers/transactions";

    private final RestTemplate restTemplate;

    private final CliContext cliContext;

    @Autowired
    public TransactionCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("List all known transactions")
    public Set<CliTransaction> transactions() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliTransaction[].class)
                .getBody())).collect(Collectors.toSet());
    }

    @ShellMethod("Show all known transactions of a customer (show-transactions CUSTOMER_NAME)")
    public String showTransactions(String name) {
        return restTemplate.getForEntity(getUriForTransaction(name), String.class).getBody();
    }

    private String getUriForTransaction(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/transactions";
    }
}
