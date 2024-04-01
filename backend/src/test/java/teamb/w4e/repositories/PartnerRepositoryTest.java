package teamb.w4e.repositories;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import teamb.w4e.entities.Partner;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PartnerRepositoryTest {

    @Autowired
    private PartnerRepository partnerRepository;

    @Test
    void testIdGenerationAndUnicity() {
        Partner partner = new Partner("partner");
        assertNull(partner.getId());
        partnerRepository.saveAndFlush(partner);
        assertNotNull(partner.getId());
        assertThrows(DataIntegrityViolationException.class, () -> partnerRepository.saveAndFlush(new Partner("partner")));
    }

    @Test
    void testFindPartnerByName() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        assertEquals(partnerRepository.findPartnerByName("partner").get(), partner);
    }

    @Test
    void testBlankName() {
        assertThrows(ConstraintViolationException.class, () -> partnerRepository.saveAndFlush(new Partner("")));
        assertThrows(ConstraintViolationException.class, () -> partnerRepository.saveAndFlush(new Partner("    ")));
    }
    }
