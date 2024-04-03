package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliPointTransaction;
import teamb.w4e.cli.model.CliTransaction;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class TransactionCommands {
    public static final String TRANSACTION_URI = "/transactions";
    public static final String POINT_TRANSACTION_URI = "/point-transactions";

    private final RestTemplate restTemplate;

    private final CliContext cliContext;

    @Autowired
    public TransactionCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("List all known transactions")
    public Set<CliTransaction> transactions() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(CustomerCommands.BASE_URI + TRANSACTION_URI, CliTransaction[].class)
                .getBody())).collect(Collectors.toSet());
    }

    @ShellMethod("List all known point transactions")
    public Set<CliPointTransaction> pointTransactions() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(CustomerCommands.BASE_URI + POINT_TRANSACTION_URI, CliPointTransaction[].class)
                .getBody())).collect(Collectors.toSet());
    }

    @ShellMethod("Show all known transactions of a customer (show-all-transactions CUSTOMER_NAME)")
    public String showAllTransactions(String name) {
        String transactions = restTemplate.getForEntity(getUriForTransaction(name), String.class).getBody();
        String pointTransactions = restTemplate.getForEntity(getUriForPointTransaction(name), String.class).getBody();
        if (transactions != null && pointTransactions != null) {
            return transactions + "\n" + pointTransactions;
        }
        return "No transactions found";
    }

    private String getUriForTransaction(String name) {
        return CustomerCommands.BASE_URI + TRANSACTION_URI + "/" + cliContext.getCustomers().get(name).getId();
    }

    private String getUriForPointTransaction(String name) {
        return CustomerCommands.BASE_URI + POINT_TRANSACTION_URI + "/" + cliContext.getCustomers().get(name).getId();
    }
}
