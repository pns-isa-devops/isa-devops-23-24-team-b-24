package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliLeisure;
import teamb.w4e.cli.model.CliPartner;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ShellComponent
public class LeisureCommands {
    public static final String BASE_URI = "/catalog";

    private final RestTemplate restTemplate;
    private final CliContext cliContext;

    @Autowired
    public LeisureCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Register leisure(register-leisure PARTNER_NAME, LEISURE_NAME DESCRIPTION PRICE)")
    public CliLeisure registerLeisure(
            String partnerName,
            String leisureName,
            String description,
            double price,
            @ShellOption(value = "-a", defaultValue = "false") boolean isActivity) {

        CliPartner partner = restTemplate.getForEntity(gerUriForPartner(partnerName), CliPartner.class).getBody();
        assert partner != null;
        if (isActivity) {
            CliLeisure newActivity = new CliLeisure(partner.getName(), leisureName, description, price, true);
            CliLeisure res = restTemplate.postForObject(BASE_URI + "/" + partner.getId() + "/activities", newActivity, CliLeisure.class);
            cliContext.getLeisure().put(Objects.requireNonNull(res).getName(), res);
            return res;
        } else {
            CliLeisure newService = new CliLeisure(partner.getName(), leisureName, description, price, false);
            CliLeisure res = restTemplate.postForObject(BASE_URI + "/" + partner.getId() + "/services", newService, CliLeisure.class);
            cliContext.getLeisure().put(Objects.requireNonNull(res).getName(), res);
            return res;
        }
    }

    @ShellMethod("List all activities")
    public Set<CliLeisure> activities() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI + "/activities", CliLeisure[].class).getBody()))
                .filter(CliLeisure::isBooked)
                .collect(Collectors.toSet());
    }

    @ShellMethod("List all services")
    public Set<CliLeisure> services() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI + "/services", CliLeisure[].class).getBody()))
                .filter(services -> !services.isBooked())
                .collect(Collectors.toSet());
    }

    @ShellMethod("Update all known leisure from server")
    public String updateLeisure() {
        Map<String, CliLeisure> activityMap = cliContext.getLeisure();
        activityMap.clear();
        activityMap.putAll(Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliLeisure[].class)
                .getBody())).collect(Collectors.toMap(CliLeisure::getName, Function.identity())));
        return activityMap.toString();
    }

    private String gerUriForPartner(String name) {
        return PartnerCommands.BASE_URI + "/" + cliContext.getPartners().get(name).getId();
    }
}
