package teamb.w4e.repositories.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.repositories.PartnerRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdvantageCatalogRepositoryTest {

    @Autowired
    private AdvantageCatalogRepository advantageCatalogRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Test
    void testIdGenerationAndUnicity() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();

        Advantage advantage = new Advantage(partner, "advantage", AdvantageType.REDUCTION, 10);
        assertNull(advantage.getId());
        advantageCatalogRepository.saveAndFlush(advantage);
        assertNotNull(advantage.getId());
        Partner finalPartner = partner;
        assertThrows(Exception.class, () -> advantageCatalogRepository.saveAndFlush(new Advantage(finalPartner, "advantage", AdvantageType.REDUCTION, 10)));
    }

    @Test
    void testFindAdvantageByName() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Advantage advantage = new Advantage(partner, "advantage", AdvantageType.REDUCTION, 10);
        advantageCatalogRepository.saveAndFlush(advantage);
        assertEquals(advantageCatalogRepository.findAdvantageByName("advantage").get(), advantage);
    }

    @Test
    void testFindAdvantageByType() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Advantage advantage = new Advantage(partner, "advantage", AdvantageType.REDUCTION, 10);
        advantageCatalogRepository.saveAndFlush(advantage);
        assertEquals(advantageCatalogRepository.findAdvantageByType(AdvantageType.REDUCTION).get(), advantage);
    }

    @Test
    void testBlankName() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(Exception.class, () -> advantageCatalogRepository.saveAndFlush(new Advantage(finalPartner, "", AdvantageType.REDUCTION, 10)));
        Partner finalPartner1 = partner;
        assertThrows(Exception.class, () -> advantageCatalogRepository.saveAndFlush(new Advantage(finalPartner1, "    ", AdvantageType.REDUCTION, 10)));
    }

    @Test
    void testNegativeOrZeroPrice() {
        Partner partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        partner = partnerRepository.findPartnerByName("partner").get();
        Partner finalPartner = partner;
        assertThrows(Exception.class, () -> advantageCatalogRepository.saveAndFlush(new Advantage(finalPartner, "advantage", AdvantageType.REDUCTION, -10)));
        assertThrows(Exception.class, () -> advantageCatalogRepository.saveAndFlush(new Advantage(finalPartner, "advantage", AdvantageType.REDUCTION, 0)));
    }


}
