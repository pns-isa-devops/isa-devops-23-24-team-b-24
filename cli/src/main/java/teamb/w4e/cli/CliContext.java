package teamb.w4e.cli;

import org.springframework.stereotype.Component;
import teamb.w4e.cli.model.CliAdvantage;
import teamb.w4e.cli.model.CliCustomer;
import teamb.w4e.cli.model.CliLeisure;
import teamb.w4e.cli.model.CliPartner;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContext {

    private final Map<String, CliCustomer> customers;
    private final Map<String, CliPartner> partners;
    private final Map<String, CliLeisure> leisure;
    private final Map<String, CliAdvantage> advantages;

    public Map<String, CliCustomer> getCustomers() {
        return customers;
    }

    public Map<String, CliPartner> getPartners() {
        return partners;
    }

    public Map<String, CliAdvantage> getAdvantages() {
        return advantages;
    }

    public Map<String, CliLeisure> getLeisure() {
        return leisure;
    }

    public CliContext() {
        customers = new HashMap<>();
        partners = new HashMap<>();
        advantages = new HashMap<>();
        leisure = new HashMap<>();
    }

    @Override
    public String toString() {
        return "CliContext{" +
                "customers=" + customers.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.joining(", ")) +
                ", partners=" + partners.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.joining(", ")) +
                ", advantages=" + advantages.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.joining(", ")) +
                ", leisure=" + leisure.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.joining(", ")) +
                '}';
    }

}
