package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.*;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.CustomerRepository;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class CartHandlerTest {

    @Autowired
    private CartProcessor cartProcessor;

    @Autowired
    private CartModifier cartModifier;

    @Autowired
    private CustomerFinder customerFinder;

    @Autowired
    private Payment payment;

    @MockBean
    private Scheduler scheduler;

    @MockBean
    private SkiPass skiPass;

    private Customer customer;
    private Activity activity;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;


    @BeforeEach
    void setUp() {
        customer = new Customer("Marcus", "1230896983");
        activity = new Activity("Tennis", "Tennis court",123, Set.of());
        customerRepository.save(customer);
        activityCatalogRepository.save(activity);
        TimeSlotItem i = new TimeSlotItem(activity, "07-11 21:30");
        Set<Item> e = new HashSet<>();
        e.add(i);
        customer.getCaddy().setLeisure(e);
    }

    @Test
    void update() {
        when(scheduler.checkAvailability(activity, "07-11 21:30")).thenReturn(true);
        when(scheduler.checkAvailability(activity, "07-11 21:31")).thenReturn(false);
    }

    @Test
    void reserve() {
        when(skiPass.reserve("ski", "day", 3)).thenReturn(java.util.Optional.of(""));
        when(skiPass.reserve("surf", "day", 3)).thenReturn(java.util.Optional.empty());
        when(skiPass.reserve("ski", "night", 3)).thenReturn(java.util.Optional.empty());
        when(skiPass.reserve("ski", "half_day", -3)).thenReturn(java.util.Optional.empty());
    }

    @Test
    void removeItem() throws IdNotFoundException, CustomerIdNotFoundException {
        assertEquals(1, customer.getCaddy().getLeisure().size());
        cartModifier.removeItem(customer.getId(), activity.getId());
        assertTrue(customer.getCaddy().getLeisure().isEmpty());
        Activity activity2 = new Activity("Football", "Football field", 123, Set.of());
        Activity activity1 = new Activity("Hockey", "Hockey field", 123, Set.of());
        activityCatalogRepository.save(activity1);
        activityCatalogRepository.save(activity2);
        TimeSlotItem i = new TimeSlotItem(activity1, "07-11 21:30");
        TimeSlotItem i2 = new TimeSlotItem(activity2, "04-07 13:00");
        Set<Item> e = new HashSet<>();
        e.add(i);
        e.add(i2);
        customer.getCaddy().setLeisure(e);
        assertEquals(2, customer.getCaddy().getLeisure().size());
        cartModifier.removeItem(customer.getId(), activity1.getId());
        assertEquals(1, customer.getCaddy().getLeisure().size());
        cartModifier.removeItem(customer.getId(), activity2.getId());
        assertEquals(0, customer.getCaddy().getLeisure().size());
    }



}
