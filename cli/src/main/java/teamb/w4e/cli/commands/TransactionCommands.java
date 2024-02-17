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
    public CliTransaction makeTransaction(String name, double amount) {
        CliTransaction res = restTemplate.postForObject(BASE_URI, new CliTransaction(name, amount), CliTransaction.class);
        cliContext.getTransactions().put(Objects.requireNonNull(res).getCustomerName(), res);
        return res;
    } // This method will remove in the future

    @ShellMethod("List all known transactions")
    public String transactions() {
        return cliContext.getTransactions().toString();
    }

    @ShellMethod("Show all known transactions of a customer (show-transactions CUSTOMER_NAME)")
    public String showTransactions(String name) {
        return restTemplate.getForEntity(getUriForTransaction(name), String.class).getBody();
    }

    private String getUriForTransaction(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/transactions";
    }
}
