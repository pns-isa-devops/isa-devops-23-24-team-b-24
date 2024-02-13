package teamb.w4e.cli;

import teamb.w4e.cli.model.CLITransaction;
import teamb.w4e.cli.model.CliCustomer;
import org.springframework.stereotype.Component;
import teamb.w4e.cli.model.CliCustomer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContext {

    private Map<String, CliCustomer> customers;

    private Map<String, CLITransaction> transactions;

    public Map<String, CliCustomer> getCustomers() {
        return customers;
    }

    public Map<String, CLITransaction> getTransactions() { return transactions; }

    public CliContext() {
        customers = new HashMap<>();
    }

    @Override
    public String toString() {
        return customers.keySet().stream()
                .map(key -> key + "=" + customers.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
