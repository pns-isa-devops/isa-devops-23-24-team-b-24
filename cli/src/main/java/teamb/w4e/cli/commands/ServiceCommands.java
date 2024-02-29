package teamb.w4e.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.cli.CliContext;
import teamb.w4e.cli.model.CliService;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ShellComponent
public class ServiceCommands {
    public static final String BASE_URI = "/catalog/services";

    private final RestTemplate restTemplate;
    private final CliContext cliContext;

    @Autowired
    public ServiceCommands(RestTemplate restTemplate, CliContext cliContext) {
        this.restTemplate = restTemplate;
        this.cliContext = cliContext;
    }

    @ShellMethod("Create a service (create-service SERVICE_NAME SERVICE_DESCRIPTION PRICE)")
    public CliService createService(String name, String description, double price) {
        CliService newService = new CliService(name, description, price);
        CliService res = restTemplate.postForObject(BASE_URI, newService, CliService.class);
        cliContext.getServices().put(Objects.requireNonNull(res).getName(), res);
        return res;
    }

    @ShellMethod("List all services")
    public Set<CliService> services() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliService[].class).getBody())).collect(Collectors.toSet());
    }

    @ShellMethod("Update all known services from server")
    public String updateServices() {
        Map<String, CliService> serviceMap = cliContext.getServices();
        serviceMap.clear();
        serviceMap.putAll(Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(BASE_URI, CliService[].class)
                .getBody())).collect(Collectors.toMap(CliService::getName, Function.identity())));
        return serviceMap.toString();

    }
}
