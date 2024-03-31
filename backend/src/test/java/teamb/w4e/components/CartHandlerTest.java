package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.TimeSlotItem;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.*;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.customers.CustomerRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class CartHandlerTest {

    @Autowired
    private PartnerRegistration partnerRegistration;
    @Autowired
    PartnerFinder partnerFinder;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;

    @MockBean
    private Scheduler scheduler;

    @MockBean
    private SkiPass skiPass;
    private Activity activity;


    @BeforeEach
    void setUp() throws AlreadyExistingException {
        partnerRegistration.register("partner");
        Partner partner = partnerFinder.findByName("partner").orElseThrow();

        Customer customer = new Customer("Marcus", "1230896983");
        activity = new Activity(partner, "Tennis", "Tennis court", 123);
        customerRepository.save(customer);
        activityCatalogRepository.save(activity);
        TimeSlotItem i = new TimeSlotItem(activity, "07-11 21:30");
        Set<Item> e = new HashSet<>();
        e.add(i);
        customer.getCaddy().setCatalogItem(e);
    }

    @Test
    void TestCheckAvailability() {
        when(scheduler.checkAvailability(activity, "07-11 21:30")).thenReturn(true);
        when(scheduler.checkAvailability(activity, "07-11 21:31")).thenReturn(false);
    }


    @Test
    void TestReserve() {
        when(skiPass.reserve("ski", "day", 3)).thenReturn(java.util.Optional.of(""));
        when(skiPass.reserve("surf", "day", 3)).thenReturn(java.util.Optional.empty());
        when(skiPass.reserve("ski", "night", 3)).thenReturn(java.util.Optional.empty());
        when(skiPass.reserve("ski", "half_day", -3)).thenReturn(java.util.Optional.empty());
    }


}
