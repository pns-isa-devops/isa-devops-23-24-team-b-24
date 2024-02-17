package teamb.w4e.interfaces;

import teamb.w4e.entities.Advantage;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface AdvantageFinder {

    Optional<Advantage> findByName(String name);

    Optional<Advantage> findByType(String type);

    Optional<Advantage> findById(Long id);

    Advantage retrieveAdvantage(Long advantageId) throws IdNotFoundException;

    List<Advantage> findAll();
}
