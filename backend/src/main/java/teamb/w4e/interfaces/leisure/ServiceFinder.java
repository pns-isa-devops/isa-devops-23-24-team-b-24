package teamb.w4e.interfaces.leisure;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ServiceFinder {
    @Query("SELECT s FROM Service s WHERE s.name = :name AND s.booked = false")
    Optional<Service> findServiceByName(@Param("name")String name);

    @Query("SELECT s FROM Service s WHERE s.id = :id AND s.booked = false")
    Optional<Service> findServiceById(@Param("id")Long id);

    @Query("SELECT s FROM Service s WHERE s.id = :serviceId AND s.booked = false")
    Service retrieveService(@Param("serviceId") Long serviceId) throws IdNotFoundException;

    @Query("SELECT s FROM Service s WHERE s.booked = false")
    List<Service> findAllServices();
}
