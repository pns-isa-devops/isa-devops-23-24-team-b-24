package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliTransaction;

import java.util.Objects;

@ShellComponent
public class TransactionCommands {

    public static final String BASE_URI = "/customers";

    private final RestTemplate restTemplate;

    private final CliContext cliContext;

    @Autowired
    public TransactionCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Execute a transaction in the CoD backend (transaction CUSTOMER_NAME AMOUNT)")
    public CliTransaction transaction(String name, double amount) {
        CliTransaction res = restTemplate.postForObject(BASE_URI, new CliTransaction(name, amount), CliTransaction.class);
        cliContext.getTransactions().put(Objects.requireNonNull(res).getCustomerName(), res);
        return res;
    }

    @ShellMethod("List all known transactions")
    public String transactions() {
        return cliContext.getTransactions().toString();
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/transactions";
    }
}
