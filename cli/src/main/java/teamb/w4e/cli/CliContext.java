package teamb.w4e.cli;

import teamb.w4e.cli.model.CliTransaction;
import teamb.w4e.cli.model.CliCustomer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContext {

    private Map<String, CliCustomer> customers;

    private Map<String, CliTransaction> transactions;

    public Map<String, CliCustomer> getCustomers() {
        return customers;
    }

    public Map<String, CliTransaction> getTransactions() {
        return transactions;
    }

    public CliContext() {
        customers = new HashMap<>();
        transactions = new HashMap<>();
    }

    @Override
    public String toString() {
        return customers.keySet().stream()
                .map(key -> key + "=" + customers.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
