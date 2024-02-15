package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliGroup;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class GroupCommands {

    public static final String BASE_URI = "/customers";

    private final RestTemplate restTemplate;

    private final CliContext cliContext;

    @Autowired
    public GroupCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Create a group in the backend (create-group LEADER_NAME MEMBER1,MEMBER2,...)")
    public CliGroup createGroup(String leaderName, String member) {
        return restTemplate.postForObject(getUriForCustomer(leaderName), new CliGroup(leaderName, Arrays.stream(member.split(",")).collect(Collectors.toSet())), CliGroup.class);
    }

    @ShellMethod("List all known groups")
    public Set<CliGroup> groups() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliGroup[].class)
                .getBody())).collect(Collectors.toSet());
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId() + "/group";
    }


}
