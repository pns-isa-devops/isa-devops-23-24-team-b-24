package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliCustomer;
import teamb.w4e.cli.model.CliGroup;

import java.util.Arrays;
import java.util.HashSet;
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

    @ShellMethod("Create a group in the backend (create-group LEADER_NAME --m MEMBER1 MEMBER2...)")
    public CliGroup createGroup(String leaderName,@ShellOption(arity = -1) String... m) {
        if (m.length == 0) {
            throw new IllegalArgumentException("At least one member is required");
        }
        ResponseEntity<CliCustomer> leaderResponse = restTemplate.getForEntity(getUriForCustomer(leaderName), CliCustomer.class);
        Set<CliCustomer> members = new HashSet<>();
        for (String member : m) {
            ResponseEntity<CliCustomer> memberResponse = restTemplate.getForEntity(getUriForCustomer(member), CliCustomer.class);
            if (memberResponse.getStatusCode() == HttpStatus.OK) {
                members.add(Objects.requireNonNull(memberResponse.getBody()));
            }
        }
        CliGroup group = new CliGroup(leaderResponse.getBody(), members);
        return restTemplate.postForObject(getUriForGroup(leaderName), group, CliGroup.class);
    }

    @ShellMethod
    public String fetch(@ShellOption(arity = -1) String... tickets) {
        return String.format("Number of tickets is %d.", tickets.length);
    }

    @ShellMethod("List all known groups")
    public Set<CliGroup> groups() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI + "/groups", CliGroup[].class)
                .getBody())).collect(Collectors.toSet());
    }

    public String getUriForGroup(String name) {
        return BASE_URI + "/groups/" + cliContext.getCustomers().get(name).getId() + "/group";
    }

    private String getUriForCustomer(String name) {
        return BASE_URI + "/" + cliContext.getCustomers().get(name).getId();
    }


}
