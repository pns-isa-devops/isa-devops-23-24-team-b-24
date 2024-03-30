package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.PartnerFinder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PartnerRegistryTest {

    @Autowired
    private PartnerRegistry partnerRegistry;

    @Autowired
    private PartnerFinder partnerFinder;
    private Partner partner;

    @BeforeEach
    void setUpContext() throws AlreadyExistingException {
        partnerRegistry.register("partner");
        partner = partnerFinder.findByName("partner").get();
    }


    @Test
    void testRegisterSuccess() throws AlreadyExistingException {
        Partner newPartner = partnerRegistry.register("newPartner");
        Partner registeredPartner = partnerFinder.findByName("newPartner").get();
        assertEquals(newPartner, registeredPartner);
    }

    @Test
    void testRegisterFailure() {
        assertThrows(AlreadyExistingException.class, () -> partnerRegistry.register("partner"));
    }

    @Test
    void testFindByName() {
        Partner foundPartner = partnerFinder.findByName("partner").get();
        assertEquals(partner, foundPartner);
        assertTrue(partnerFinder.findByName("notExisting").isEmpty());
    }

    @Test
    void findById() {
        Partner foundPartner = partnerFinder.findById(partner.getId()).get();
        assertEquals(partner, foundPartner);
        assertTrue(partnerFinder.findById(2L).isEmpty());
    }

    @Test
    void retrievePartner() throws IdNotFoundException {
        Partner foundPartner = partnerFinder.retrievePartner(partner.getId());
        assertEquals(partner, foundPartner);
    }

    @Test
    void findAll() {
        assertEquals(1, partnerFinder.findAll().size());
    }
}
