package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliActivity;
import teamb.w4e.cli.model.CliAdvantage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;


@ShellComponent
public class ActivityCommands {
    public static final String BASE_URI = "/catalog/activities";

    private final RestTemplate restTemplate;
    private final CliContext cliContext;

    @Autowired
    public ActivityCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Create an activity (create-activity ACTIVITY_NAME ACTIVITY_DESCRIPTION PRICE [ADVANTAGE_NAME,ADVANTAGE_NAME,...])")
    public CliActivity createActivity(String name, String description, double price, @ShellOption(defaultValue = "") String advantageNames) {
        Set<CliAdvantage> advantagesSet = new HashSet<>();
        if (!advantageNames.isEmpty()) {
            String[] advantageNameArray = advantageNames.split(",");
            for (String advantageName : advantageNameArray) {
                ResponseEntity<CliAdvantage> response = restTemplate.getForEntity(getUriForAdvantage(advantageName), CliAdvantage.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    advantagesSet.add(convertToCliAdvantage(Objects.requireNonNull(response.getBody())));
                }
            }
        }
        CliActivity newActivity = new CliActivity(name, description, price, advantagesSet);
        CliActivity res = restTemplate.postForObject(BASE_URI, newActivity, CliActivity.class);
        cliContext.getActivities().put(Objects.requireNonNull(res).getName(), res);
        return res;
    }

    @ShellMethod("List all activities")
    public Set<CliActivity> activities() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliActivity[].class).getBody())).collect(Collectors.toSet());
    }

    @ShellMethod("Update all known activities from server")
    public String updateActivities() {
        Map<String, CliActivity> activityMap = cliContext.getActivities();
        activityMap.clear();
        activityMap.putAll(Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliActivity[].class)
                .getBody())).collect(toMap(CliActivity::getName, Function.identity())));
        return activityMap.toString();
    }

    private String getUriForAdvantage(String name) {
        return AdvantageCommands.BASE_URI + "/" + cliContext.getAdvantages().get(name).getId();
    }


    private CliAdvantage convertToCliAdvantage(CliAdvantage advantage) {
        return new CliAdvantage(advantage.getName(), advantage.getType(), advantage.getPoints());
    }
}
