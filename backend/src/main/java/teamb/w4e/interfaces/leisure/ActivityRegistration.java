package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;

public interface ActivityRegistration {
    Activity registerActivity(Long partnerId, String name, String description, double price) throws IdNotFoundException, AlreadyExistingException;
}
