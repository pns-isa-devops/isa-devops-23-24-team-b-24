package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.catalog.Service;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;

public interface ServiceRegistration {
    Service registerService(Long partnerId, String name, String description, double price) throws IdNotFoundException, AlreadyExistingException;
}
