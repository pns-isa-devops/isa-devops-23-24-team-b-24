package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Advantage;
import teamb.w4e.entities.AdvantageType;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.AdvantageFinder;
import teamb.w4e.interfaces.AdvantageRegistration;
import teamb.w4e.repositories.CatalogRepository;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class Catalog implements AdvantageRegistration, AdvantageFinder {

    private final CatalogRepository catalogRepository;

    @Autowired
    public Catalog(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    @Transactional
    public Advantage register(String name, AdvantageType type, int points) {
        Advantage newAdvantage = new Advantage(name, type, points);
        return catalogRepository.save(newAdvantage);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AdvantageType> listAdvantageTypes() {
        return EnumSet.allOf(AdvantageType.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findByName(String name) {
        return catalogRepository.findAdvantageByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findByType(AdvantageType type) {
        return catalogRepository.findAdvantageByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findById(Long id) {
        return catalogRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Advantage retrieveAdvantage(Long advantageId) throws IdNotFoundException {
        return findById(advantageId).orElseThrow(() -> new IdNotFoundException(advantageId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Advantage> findAll() {
        return catalogRepository.findAll();
    }
}
