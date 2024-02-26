package teamb.w4e.repositories;

import org.springframework.stereotype.Repository;
import teamb.w4e.entities.Service;

import java.util.Optional;

@Repository
public interface ServiceCatalogRepository {
    Optional<Service> findServiceByName(String name);
}
