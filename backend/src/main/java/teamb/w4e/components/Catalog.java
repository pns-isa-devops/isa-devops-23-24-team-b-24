package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Advantage;
import teamb.w4e.entities.AdvantageType;
import teamb.w4e.interfaces.AdvantageRegistration;
import teamb.w4e.repositories.CatalogRepository;

import java.util.Set;

@Service
public class Catalog implements AdvantageRegistration {

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
}
