package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.TimeSlotItem;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.interfaces.*;
import teamb.w4e.interfaces.leisure.ActivityFinder;
import teamb.w4e.interfaces.leisure.ActivityRegistration;
import teamb.w4e.repositories.customers.CustomerRepository;
import teamb.w4e.repositories.transactions.PointTransactionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

        advantageRegistration.register(partner,"Thé citroné", AdvantageType.LOCAL_SPECIALITY, 2);
        localSpeciality = advantageFinder.findByName("Thé citroné").orElseThrow(() -> new IdNotFoundException(1L));

        advantageRegistration.register(partner,"Réduction", AdvantageType.REDUCTION, 5);
        reduction = advantageFinder.findByName("Réduction").orElseThrow(() -> new IdNotFoundException(2L));


    }

    @Test
    void testApply() throws NegativeAmountTransactionException, AlreadyExistingException {
        customer.getCard().addPoints(10);
        customer.getAdvantageCaddy().addAdvantage(localSpeciality);
        assertFalse(customer.getAdvantageCaddy().getAdvantages().isEmpty());
        PointTransaction pointTransaction = advantageApplier.apply(customer, localSpeciality, activity);
        assertTrue(customer.getAdvantageCaddy().getAdvantages().isEmpty());
        assertTrue(customer.getCaddy().getLeisure().stream().anyMatch(item -> item.getLeisure().getName().equals("Thé citroné")));
        List<PointTransaction> pointTransactions = pointTransactionRepository.findAll();
        assertEquals(1, pointTransactions.size());
        assertEquals(pointTransactions.get(0), pointTransaction);
        assertEquals(8, customer.getCard().getPoints());
    }

    @Test
    void testApplyWithException() throws AlreadyExistingException {
        customer.getAdvantageCaddy().addAdvantage(reduction);
        assertThrows(NegativeAmountTransactionException.class, () -> advantageApplier.apply(customer, reduction, activity));
    }

    @Test
    void testReduction() throws NegativeAmountTransactionException, IdNotFoundException, AlreadyExistingException {
        int point = 10;
        customer.getCard().addPoints(point);
        customer.getAdvantageCaddy().addAdvantage(reduction);
        TimeSlotItem i = new TimeSlotItem(activity, "07-11 21:30");
        double price = i.getLeisure().getPrice();
        Set<Item> e = new HashSet<>();
        e.add(i);
        customer.getCaddy().setLeisure(e);
        assertFalse(customer.getAdvantageCaddy().getAdvantages().isEmpty());
        PointTransaction pointTransaction = advantageApplier.reduction(customer, reduction, activity);
        assertTrue(customer.getAdvantageCaddy().getAdvantages().isEmpty());
        List<PointTransaction> pointTransactions = pointTransactionRepository.findAll();
        assertEquals(1, pointTransactions.size());
        assertEquals(pointTransactions.get(0), pointTransaction);
        assertEquals(5, customer.getCard().getPoints());
        assertEquals(price - pointAdder.convertPointsToPrice(reduction.getPoints()), customer.getCaddy().getLeisure().stream().filter(item -> item.getLeisure().equals(activity)).findFirst().orElseThrow().getLeisure().getPrice());
    }

    @Test
    void testReductionWithEmptyCart() throws AlreadyExistingException {
        customer.getCard().addPoints(10);
        customer.getAdvantageCaddy().addAdvantage(reduction);
        assertFalse(customer.getAdvantageCaddy().getAdvantages().isEmpty());
        assertThrows(IdNotFoundException.class, () -> advantageApplier.reduction(customer, reduction, activity));
    }

    @Test
    void testReductionNotValidAmount() throws AlreadyExistingException {
        customer.getAdvantageCaddy().addAdvantage(localSpeciality);
        assertThrows(NegativeAmountTransactionException.class, () -> advantageApplier.reduction(customer, localSpeciality, activity));
    }

}
