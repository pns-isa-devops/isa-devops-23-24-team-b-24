package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.Set;

public interface ActivityRegistration {
    Activity register(String name, String description, double price, Set<Advantage> advantages);

    String deleteActivity(Long id) throws IdNotFoundException;
}
