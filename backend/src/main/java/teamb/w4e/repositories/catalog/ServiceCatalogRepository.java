package teamb.w4e.repositories.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamb.w4e.entities.catalog.Service;

import java.util.Optional;

@Repository
public interface ServiceCatalogRepository extends JpaRepository<Service, Long> {
    Optional<Service> findServiceByName(String name);
}
