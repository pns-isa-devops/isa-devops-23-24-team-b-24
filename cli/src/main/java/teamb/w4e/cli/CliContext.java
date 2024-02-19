package teamb.w4e.cli;

import org.springframework.stereotype.Component;
import teamb.w4e.cli.model.CliActivity;
import teamb.w4e.cli.model.CliAdvantage;
import teamb.w4e.cli.model.CliCustomer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContext {

    private Map<String, CliCustomer> customers;
    private Map<String, CliActivity> activities;

    private Map<String, CliAdvantage> advantages;

    public Map<String, CliCustomer> getCustomers() {
        return customers;
    }

    public Map<String, CliActivity> getActivities() {
        return activities;
    }

    public Map<String, CliAdvantage> getAdvantages() {
        return advantages;
    }

    public CliContext() {
        customers = new HashMap<>();
        activities = new HashMap<>();
        advantages = new HashMap<>();
    }

    @Override
    public String toString() {
        return customers.keySet().stream()
                .map(key -> key + "=" + customers.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
