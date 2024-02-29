package teamb.w4e.cucumber.catalog;

import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrowsingCatalog {

    @When("one check the catalog")
    public void oneCheckTheCatalog() {
        // return true but it's just for the example
        assertTrue(true);
    }
}
