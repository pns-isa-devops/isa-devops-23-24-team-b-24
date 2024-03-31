package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.AdvantageItem;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.TimeSlotItem;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.*;
import teamb.w4e.interfaces.leisure.ActivityFinder;
import teamb.w4e.interfaces.leisure.ActivityRegistration;
import teamb.w4e.repositories.customers.CustomerRepository;
import teamb.w4e.repositories.transactions.PointTransactionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class ApplierTest {

    @Autowired
    private PartnerRegistration partnerRegistration;
    @Autowired
    private PartnerFinder partnerFinder;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ActivityRegistration activityRegistration;
    @Autowired
    private ActivityFinder activityFinder;
    @Autowired
    private AdvantageRegistration advantageRegistration;
    @Autowired
    AdvantageFinder advantageFinder;
    @Autowired
    private PointTransactionRepository pointTransactionRepository;
    @Autowired
    private AdvantageApplier advantageApplier;
    @Autowired
    private PointAdder pointAdder;
    @Autowired
    private CartModifier cartModifier;

    @MockBean
    private Scheduler scheduler;
    private Customer customer;
    private Activity activity;
    private Advantage localSpeciality, reduction;


    @BeforeEach
    void setUp() throws AlreadyExistingException, IdNotFoundException {
        partnerRegistration.register("partner");
        Partner partner = partnerFinder.findByName("partner").orElseThrow(() -> new IdNotFoundException(1L));

        customer = new Customer("Hana", "1230896983");
        customerRepository.save(customer);

        activityRegistration.registerActivity(partner.getId(), "Tennis", "Tennis court", 10);
        activity = activityFinder.findActivityByName("Tennis").orElseThrow(() -> new IdNotFoundException(1L));

        advantageRegistration.register(partner, "Thé citroné", AdvantageType.LOCAL_SPECIALITY, 2);
        localSpeciality = advantageFinder.findByName("Thé citroné").orElseThrow(() -> new IdNotFoundException(1L));

        advantageRegistration.register(partner, "Réduction", AdvantageType.REDUCTION, 5);
        reduction = advantageFinder.findByName("Réduction").orElseThrow(() -> new IdNotFoundException(2L));


    }

    @Test
    void testApply() throws NegativeAmountTransactionException, AlreadyExistingException, IdNotFoundException, NonValidDateForActivity {
        customer.getCard().addPoints(10);
        cartModifier.advantageUpdate(customer.getId(), localSpeciality);
        assertFalse(cartModifier.advantageCartContent(customer.getId()).isEmpty());
        when(scheduler.checkAvailability(activity, "07-11 21:30")).thenReturn(true);
        TimeSlotItem timeSlotItem = cartModifier.timeSlotUpdate(customer.getId(), activity, "07-11 21:30");
        PointTransaction pointTransaction = advantageApplier.apply(customer, localSpeciality, timeSlotItem);
        assertTrue(cartModifier.advantageCartContent(customer.getId()).isEmpty());
        assertTrue(cartModifier.cartContent(customer.getId()).stream().anyMatch(item -> item.getName().equals("Thé citroné")));
        List<PointTransaction> pointTransactions = pointTransactionRepository.findAll();
        assertEquals(1, pointTransactions.size());
        assertEquals(pointTransactions.get(0), pointTransaction);
        assertEquals(8, customer.getCard().getPoints());
    }

    @Test
    void testApplyWithException() throws NonValidDateForActivity, IdNotFoundException {
        when(scheduler.checkAvailability(activity, "07-11 21:30")).thenReturn(true);
        TimeSlotItem timeSlotItem = cartModifier.timeSlotUpdate(customer.getId(), activity, "07-11 21:30");
        customer.getCaddy().setCatalogItem(Set.of(new AdvantageItem(reduction), timeSlotItem));
        assertThrows(NegativeAmountTransactionException.class, () -> advantageApplier.apply(customer, reduction, timeSlotItem));
    }

    @Test
    void testReduction() throws NegativeAmountTransactionException, IdNotFoundException, AlreadyExistingException, NonValidDateForActivity, NotFoundException {
        int point = 10;
        customer.getCard().addPoints(point);
        TimeSlotItem timeSlotItem = new TimeSlotItem(activity, "07-11 21:30");
        Set<Item> items = new HashSet<>();
        items.add(new AdvantageItem(reduction));
        items.add(timeSlotItem);
        customer.getCaddy().setCatalogItem(items);
        PointTransaction pointTransaction = advantageApplier.reduction(customer, reduction, timeSlotItem);
        assertTrue(cartModifier.advantageCartContent(customer.getId()).isEmpty());
        assertEquals(1, cartModifier.cartContent(customer.getId()).size());
        List<PointTransaction> pointTransactions = pointTransactionRepository.findAll();
        assertEquals(1, pointTransactions.size());
        assertEquals(pointTransactions.get(0), pointTransaction);
        assertEquals(5, customer.getCard().getPoints());
        assertEquals(activity.getPrice() - pointAdder.convertPointsToPrice(reduction.getPoints()), customer.getCaddy().getCatalogItem().stream().filter(item -> item.getName().equals("Tennis")).findFirst().orElseThrow().getAmount());
    }

    @Test
    void testReductionWithEmptyCart() throws AlreadyExistingException, NegativeAmountTransactionException, IdNotFoundException, NonValidDateForActivity {
        customer.getCard().addPoints(10);
        cartModifier.advantageUpdate(customer.getId(), reduction);
        TimeSlotItem timeSlotItem = new TimeSlotItem(activity, "07-11 21:30");
        assertFalse(customer.getCaddy().getCatalogItem().isEmpty());
        assertThrows(NotFoundException.class, () -> advantageApplier.reduction(customer, reduction, timeSlotItem));
    }

    @Test
    void testReductionNotValidAmount() throws NonValidDateForActivity, IdNotFoundException {
        when(scheduler.checkAvailability(activity, "07-11 21:30")).thenReturn(true);
        TimeSlotItem timeSlotItem = cartModifier.timeSlotUpdate(customer.getId(), activity, "07-11 21:30");
        customer.getCaddy().setCatalogItem(Set.of(new AdvantageItem(reduction)));
        assertThrows(NegativeAmountTransactionException.class, () -> advantageApplier.reduction(customer, localSpeciality, timeSlotItem));
    }

}
