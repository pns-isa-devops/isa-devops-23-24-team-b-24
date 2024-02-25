package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NonValidDateForActivity;
import teamb.w4e.interfaces.*;
import teamb.w4e.repositories.ActivityCatalogRepository;
import teamb.w4e.repositories.CustomerRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private Customer customer;
    private Activity activity;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;


    @BeforeEach
    void setUp() {
        customer = new Customer("Marcus", "1230896983");
        activity = new Activity("Tennis", "Tennis court", Set.of());
        customerRepository.save(customer);
        activityCatalogRepository.save(activity);

    }

    @Test
    void update() throws NonValidDateForActivity, IdNotFoundException {
        cartModifier.update(customer.getId(), activity, "07-11 21:30");
        assertTrue(customerFinder.retrieveCustomer(customer.getId()).getCaddy().getActivities().size() == 1);
        assertThrows(NonValidDateForActivity.class, () -> cartModifier.update(customer.getId(), activity, "04-03 10:40"));
    }



}
