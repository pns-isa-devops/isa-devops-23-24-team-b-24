package teamb.w4e.repositories.catalog;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.repositories.PartnerRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ActivityCatalogRepositoryTest {

    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Test
    void testIdGenerationAndUnicity() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Activity activity = new Activity(partner, "activity", "description", 10);
        assertNull(activity.getId());
        activityCatalogRepository.saveAndFlush(activity);
        assertNotNull(activity.getId());
        Partner finalPartner = partner;
        assertThrows(DataIntegrityViolationException.class, () -> activityCatalogRepository.saveAndFlush(new Activity(finalPartner, "activity", "description", 10)));
    }

    @Test
    void testFindActivityByName() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Activity activity = new Activity(partner, "activity", "description", 10);
        activityCatalogRepository.saveAndFlush(activity);
        assertEquals(activityCatalogRepository.findActivityByName("activity").get(), activity);
    }

    @Test
    void testBlankName() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(ConstraintViolationException.class, () -> activityCatalogRepository.saveAndFlush(new Activity(finalPartner, "", "description", 10)));
        Partner finalPartner1 = partner;
        assertThrows(ConstraintViolationException.class, () -> activityCatalogRepository.saveAndFlush(new Activity(finalPartner1, "    ", "description", 10)));
    }

    @Test
    void testBlankDescription() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(ConstraintViolationException.class, () -> activityCatalogRepository.saveAndFlush(new Activity(finalPartner, "activity", "", 10)));
        Partner finalPartner1 = partner;
        assertThrows(ConstraintViolationException.class, () -> activityCatalogRepository.saveAndFlush(new Activity(finalPartner1, "activity", "    ", 10)));
    }

    @Test
    void testNegativePrice() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(ConstraintViolationException.class, () -> activityCatalogRepository.saveAndFlush(new Activity(finalPartner, "activity", "description", -10)));
    }

    @Test
    void testZeroPrice() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertDoesNotThrow(() -> activityCatalogRepository.saveAndFlush(new Activity(finalPartner, "activity", "description", 0)));
    }
}
