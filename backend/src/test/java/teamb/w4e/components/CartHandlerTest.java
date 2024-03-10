package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NonValidDateForActivity;
import teamb.w4e.interfaces.*;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.CustomerRepository;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
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
        customer.getCaddy().setActivities(e);


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



}
