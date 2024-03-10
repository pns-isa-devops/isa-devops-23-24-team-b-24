package teamb.w4e.cli;

import org.springframework.stereotype.Component;
import teamb.w4e.cli.model.CliAdvantage;
import teamb.w4e.cli.model.CliCustomer;
import teamb.w4e.cli.model.CliLeisure;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContext {

    private final Map<String, CliCustomer> customers;
    private final Map<String, CliLeisure> leisure;
    private final Map<String, CliAdvantage> advantages;

    public Map<String, CliCustomer> getCustomers() {
        return customers;
    }

    public Map<String, CliAdvantage> getAdvantages() {
        return advantages;
    }

    public Map<String, CliLeisure> getLeisure() {
        return leisure;
    }

    public CliContext() {
        customers = new HashMap<>();
        advantages = new HashMap<>();
        leisure = new HashMap<>();
    }

    @Override
    public String toString() {
        return customers.keySet().stream()
                .map(key -> key + "=" + customers.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
