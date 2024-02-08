package teamb.w4e.interfaces;

import teamb.w4e.entities.Cookies;

import java.util.Set;

public interface CatalogExplorator {

    Set<Cookies> listPreMadeRecipes();

    Set<Cookies> exploreCatalogue(String regexp);

}

