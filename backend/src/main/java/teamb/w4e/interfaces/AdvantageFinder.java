package teamb.w4e.interfaces;

import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdvantageFinder {

    Set<AdvantageType> listAdvantageTypes();

    Optional<Advantage> findByName(String name);

    Optional<Advantage> findByType(AdvantageType type);

    Optional<Advantage> findAdvantageById(Long id);

    Advantage retrieveAdvantage(Long advantageId) throws IdNotFoundException;

    List<Advantage> findAllAdvantages();
}
