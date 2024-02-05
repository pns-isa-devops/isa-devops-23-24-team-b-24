package teamb.w4e.controllers;

import teamb.w4e.entities.Cookies;
import teamb.w4e.interfaces.CatalogExplorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class RecipeController {

    public static final String BASE_URI = "/recipes";

    private final CatalogExplorator catalogExp;

    @Autowired
    public RecipeController(CatalogExplorator catalogExp) {
        this.catalogExp = catalogExp;
    }

    @GetMapping(path = RecipeController.BASE_URI, produces = APPLICATION_JSON_VALUE)
    public Set<Cookies> listAllRecipes() {
        return catalogExp.listPreMadeRecipes();
    }
}
