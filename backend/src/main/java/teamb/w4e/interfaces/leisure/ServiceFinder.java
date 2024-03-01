package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.catalog.Service;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ServiceFinder {
    Optional<Service> findServiceByName(String name);
    Optional<Service> findServiceById(Long id);
    Service retrieveService(Long serviceId) throws IdNotFoundException;
    List<Service> findAllServices();
}
