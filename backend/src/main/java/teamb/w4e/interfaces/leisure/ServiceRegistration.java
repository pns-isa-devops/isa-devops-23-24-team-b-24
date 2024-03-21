package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.Set;

public interface ServiceRegistration {
    Service registerService(String name, String description, double price, Set<Advantage> advantages);

    String deleteService(Long id) throws IdNotFoundException;
}
