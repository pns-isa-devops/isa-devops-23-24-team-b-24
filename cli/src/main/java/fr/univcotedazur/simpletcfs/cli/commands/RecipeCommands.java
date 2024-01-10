package fr.univcotedazur.simpletcfs.cli.commands;

import fr.univcotedazur.simpletcfs.cli.model.CookieEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ShellComponent
public class RecipeCommands {

    private final RestTemplate restTemplate;

    @Autowired
    public RecipeCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("List all available recipes")
    public Set<CookieEnum> recipes() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity("/recipes", CookieEnum[].class)
                .getBody())).collect(toSet());
    }

}
