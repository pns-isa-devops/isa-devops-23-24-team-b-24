package teamb.w4e.repositories.catalog;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.repositories.PartnerRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ServiceCatalogRepositoryTest {
    @Autowired
    private ServiceCatalogRepository serviceCatalogRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Test
    void testIdGenerationAndUnicity() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Service service = new Service(partner, "service", "description", 10);
        assertNull(service.getId());
        serviceCatalogRepository.saveAndFlush(service);
        assertNotNull(service.getId());
        Partner finalPartner = partner;
        assertThrows(DataIntegrityViolationException.class, () -> serviceCatalogRepository.saveAndFlush(new Service(finalPartner, "service", "description", 10)));
    }

    @Test
    void testFindActivityByName() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Service service = new Service(partner, "service", "description", 10);
        serviceCatalogRepository.saveAndFlush(service);
        assertEquals(serviceCatalogRepository.findServiceByName("service").get(), service);
    }

    @Test
    void testBlankName() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(ConstraintViolationException.class, () -> serviceCatalogRepository.saveAndFlush(new Service(finalPartner, "", "description", 10)));
        Partner finalPartner1 = partner;
        assertThrows(ConstraintViolationException.class, () -> serviceCatalogRepository.saveAndFlush(new Service(finalPartner1, "    ", "description", 10)));
    }

    @Test
    void testBlankDescription() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(ConstraintViolationException.class, () -> serviceCatalogRepository.saveAndFlush(new Service(finalPartner, "service", "", 10)));
        Partner finalPartner1 = partner;
        assertThrows(ConstraintViolationException.class, () -> serviceCatalogRepository.saveAndFlush(new Service(finalPartner1, "service", "    ", 10)));
    }

    @Test
    void testNegativePrice() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(ConstraintViolationException.class, () -> serviceCatalogRepository.saveAndFlush(new Service(finalPartner, "service", "description", -10)));
    }

    @Test
    void testZeroPrice() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertDoesNotThrow(() -> serviceCatalogRepository.saveAndFlush(new Service(finalPartner, "service", "description", 0)));
    }

}
