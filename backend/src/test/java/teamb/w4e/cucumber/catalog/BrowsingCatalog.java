package teamb.w4e.cucumber.catalog;

import teamb.w4e.entities.Cookies;
import teamb.w4e.interfaces.CatalogExplorator;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrowsingCatalog {

    @Autowired
    private CatalogExplorator catalogExplorator;

    private Set<Cookies> cookiesSet;

    @When("one check the catalog contents")
    public void oneCheckTheCatalogContents() {
        cookiesSet = catalogExplorator.listPreMadeRecipes();
    }

    @Then("^there (?:is|are) (\\d+) items? in it$")
    public void thereAreItemsInIt(int itemsNb) {
        assertEquals(itemsNb, cookiesSet.size());
    }

}
