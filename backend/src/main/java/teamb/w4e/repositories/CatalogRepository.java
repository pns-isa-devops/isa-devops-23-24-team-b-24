package teamb.w4e.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamb.w4e.entities.Advantage;

import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Advantage, Long> {
    Optional<Advantage> findAdvantageByName(String name);

    Optional<Advantage> findAdvantageByType(String type);
}
