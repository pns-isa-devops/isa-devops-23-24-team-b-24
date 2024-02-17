package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.model.AdvantageType;
import teamb.w4e.cli.model.CliAdvantage;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class AdvantageCommands {

    public static final String BASE_URI = "/catalog/advantages";

    private final RestTemplate restTemplate;

    @Autowired
    public AdvantageCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Create an advantage (create-advantage ADVANTAGE_NAME ADVANTAGE_TYPE NB_POINTS)")
    public CliAdvantage createAdvantage(String name, AdvantageType type, int points) {
        return restTemplate.postForObject(BASE_URI, new CliAdvantage(name, type, points), CliAdvantage.class);
    }

    @ShellMethod("List all advantages (advantages)")
    public Set<CliAdvantage> advantages() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliAdvantage[].class)
                .getBody())).collect(Collectors.toSet());
    }

    @ShellMethod("List all types of advantages (advantage-types)")
    public Set<AdvantageType> showAdvantageTypes() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI + "/types", AdvantageType[].class)
                .getBody())).collect(Collectors.toSet());
    }

    @ShellMethod("List all advantages of a type (advantages-of-type ADVANTAGE_TYPE)")
    public Set<CliAdvantage> advantagesOfType(AdvantageType type) {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI + "/types/" + type.ordinal(), CliAdvantage[].class)
                .getBody())).collect(Collectors.toSet());
    }
}
