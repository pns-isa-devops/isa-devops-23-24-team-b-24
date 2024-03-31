package teamb.w4e.cli.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.*;

import java.util.Arrays;
import java.util.Set;

@ShellComponent
public class PointCommands {

    private final RestTemplate restTemplate;

    private final CliContext cliContext;

    public PointCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Trade points between two members of a group (trade-points SENDER_NAME RECEIVER_NAME AMOUNT)")
    public CliPointTransaction tradePoints(String senderName, String receiverName, int amount) {
        CliCustomer sender = restTemplate.getForEntity(getUriForCustomer(senderName), CliCustomer.class).getBody();
        CliCustomer receiver = restTemplate.getForEntity(getUriForCustomer(receiverName), CliCustomer.class).getBody();
        PointTrade pointTrade = new PointTrade(sender, receiver, amount);
        return restTemplate.postForObject(CustomerCommands.BASE_URI + GroupCommands.GROUP_URI + "/trade", pointTrade, CliPointTransaction.class);
    }

    @ShellMethod("Use advantage for a leisure (use-advantage CUSTOMER_NAME ADVANTAGE_NAME LEISURE_NAME)")
    public CliPointTransaction useAdvantage(String customerName, String advantageName, String leisureName,
                                            @ShellOption(value = "-a", defaultValue = "false") boolean isActivity) {
        CliAdvantage[] advantages = restTemplate.getForEntity(getUriForCustomer(customerName) + "/cart/advantage", CliAdvantage[].class).getBody();
        assert advantages != null;
        CliAdvantage advantage = Arrays.stream(advantages)
                .filter(a -> a.getName().equals(advantageName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Advantage not found"));
        CliLeisure leisure = isActivity ? restTemplate.getForEntity(getUriForActivity(leisureName), CliLeisure.class).getBody()
                : restTemplate.getForEntity(getUriForService(leisureName), CliLeisure.class).getBody();
        AppliedAdvantage appliedAdvantage = new AppliedAdvantage(advantage, leisure);
        return restTemplate.postForObject(getUriForCustomer(customerName) + "/advantage-cart", appliedAdvantage, CliPointTransaction.class);
    }


    private String getUriForCustomer(String name) {
        return CustomerCommands.BASE_URI + "/" + cliContext.getCustomers().get(name).getId();
    }

    private String getUriForActivity(String name) {
        return LeisureCommands.BASE_URI + "/activities/" + cliContext.getLeisure().get(name).getId();
    }

    private String getUriForService(String name) {
        return LeisureCommands.BASE_URI + "/services/" + cliContext.getLeisure().get(name).getId();
    }


}
