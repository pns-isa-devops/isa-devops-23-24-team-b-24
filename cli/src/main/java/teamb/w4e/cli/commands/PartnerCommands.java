package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliPartner;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@ShellComponent
public class PartnerCommands {

    public static final String BASE_URI = "/partners";

    private final RestTemplate restTemplate;

    private final CliContext cliContext;

    @Autowired
    public PartnerCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Register a partner in the CoD backend (register PARTNER_NAME)")
    public CliPartner registerPartner(String name) {
        CliPartner res = restTemplate.postForObject(BASE_URI, new CliPartner(name, null), CliPartner.class);
        cliContext.getPartners().put(Objects.requireNonNull(res).getName(), res);
        return res;
    }

    @ShellMethod("List all known partners")
    public String partners() {
        return cliContext.getPartners().toString();
    }

    @ShellMethod("Update all known partners from server")
    public String updatePartners() {
        Map<String, CliPartner> partnerMap = cliContext.getPartners();
        partnerMap.clear();
        partnerMap.putAll(Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliPartner[].class)
                .getBody())).collect(toMap(CliPartner::getName, Function.identity())));
        return partnerMap.toString();
    }
}
