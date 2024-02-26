package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Advantage;
import teamb.w4e.entities.AdvantageType;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.leisure.ActivityFinder;
import teamb.w4e.interfaces.leisure.ActivityRegistration;
import teamb.w4e.interfaces.AdvantageFinder;
import teamb.w4e.interfaces.AdvantageRegistration;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.catalog.AdvantageCatalogRepository;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class Catalog implements AdvantageRegistration, AdvantageFinder, ActivityRegistration, ActivityFinder {

    private final AdvantageCatalogRepository advantageCatalogRepository;
    private final ActivityCatalogRepository activityCatalogRepository;

    @Autowired
    public Catalog(AdvantageCatalogRepository catalogRepository, ActivityCatalogRepository activityCatalogRepository) {
        this.advantageCatalogRepository = catalogRepository;
        this.activityCatalogRepository = activityCatalogRepository;
    }

    @Override
    @Transactional
    public Advantage register(String name, AdvantageType type, int points) {
        if (type == null || points < 1) {
            throw new IllegalArgumentException("Invalid Advantage");
        }
        Advantage newAdvantage = new Advantage(name, type, points);
        return advantageCatalogRepository.save(newAdvantage);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AdvantageType> listAdvantageTypes() {
        return EnumSet.allOf(AdvantageType.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findByName(String name) {
        return advantageCatalogRepository.findAdvantageByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findByType(AdvantageType type) {
        return advantageCatalogRepository.findAdvantageByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findAdvantageById(Long id) {
        return advantageCatalogRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Advantage retrieveAdvantage(Long advantageId) throws IdNotFoundException {
        return findAdvantageById(advantageId).orElseThrow(() -> new IdNotFoundException(advantageId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Advantage> findAllAdvantages() {
        return advantageCatalogRepository.findAll();
    }

    @Override
    @Transactional
    public Activity register(String name, String description, double price, Set<Advantage> advantages) {
        Activity newActivity = new Activity(name, description,price, advantages);
        return activityCatalogRepository.save(newActivity);
    }

    @Override
    public Optional<Activity> findActivityByName(String name) {
        return activityCatalogRepository.findActivityByName(name);
    }

    @Override
    public Optional<Activity> findActivityById(Long id)  {
        return activityCatalogRepository.findById(id);
    }

    @Override
    public Activity retrieveActivity(Long activityId) throws IdNotFoundException {
        return findActivityById(activityId).orElseThrow(() -> new IdNotFoundException(activityId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Activity> findAllActivities() {
        return activityCatalogRepository.findAll();
    }

}
