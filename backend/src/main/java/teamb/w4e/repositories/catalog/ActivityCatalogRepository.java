package teamb.w4e.repositories.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamb.w4e.entities.Activity;

import java.util.Optional;

@Repository
public interface ActivityCatalogRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findActivityByName(String name);

}
