package teamb.w4e.cucumber.reservation;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.TimeSlotItem;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.*;
import teamb.w4e.interfaces.leisure.ActivityFinder;
import teamb.w4e.interfaces.leisure.ActivityRegistration;
import teamb.w4e.interfaces.reservation.ReservationFinder;
import teamb.w4e.repositories.PartnerRepository;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.customers.CustomerRepository;
import teamb.w4e.repositories.reservations.ReservationRepository;
import teamb.w4e.repositories.transactions.TransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class ReserveActivity {

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private CustomerFinder customerFinder;

    @Autowired
    private PartnerRegistration partnerRegistration;

    @Autowired
    private PartnerFinder partnerFinder;

    @Autowired
    private ActivityRegistration activityRegistration;

    @Autowired
    private ActivityFinder activityFinder;

    @Autowired
    private Scheduler schedulerMock;

    @Autowired
    private Bank bankMock;
    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    @Autowired
    private ActivityCatalogRepository activityRepository;

    @Autowired
    private CartProcessor cartProcessor;

    @Autowired
    private CartModifier cartModifier;

    @Autowired
    private TransactionFinder transactionFinder;

    @Autowired
    private ReservationFinder reservationFinder;

    private Customer customer;
    private Partner partner;
    private Activity activity;

    @Before
    public void settingUpContext() {
        customerRepository.deleteAll();
        transactionRepository.deleteAll();
        reservationRepository.deleteAll();
        activityRepository.deleteAll();
        partnerRepository.deleteAll();
        when(schedulerMock.checkAvailability(any(Activity.class), eq("07-11 21:30"))).thenReturn(true);
        when(schedulerMock.reserve(any(Activity.class), eq("07-11 21:30"))).thenReturn(true);
        when(bankMock.pay(any(Customer.class), anyDouble())).thenReturn(Optional.of("playReceiptOKId"));
    }

    @Given("a Customer named {string} with the credit card {string}")
    public void setupCustomer(String customerName, String creditCardNumber) throws AlreadyExistingException {
        customerRegistration.register(customerName, creditCardNumber);
        customer = customerFinder.findByName(customerName).get();

    }

    @And("a Partner named {string}")
    public void setupPartner(String partnerName) throws AlreadyExistingException {
        partnerRegistration.register(partnerName);
        partner = partnerFinder.findByName(partnerName).get();
    }

    @And("a Activity named {string}, {string} with a price of {double}")
    public void setupActivity(String activityName, String description, double price) throws AlreadyExistingException, IdNotFoundException {
        activityRegistration.registerActivity(partner.getId(), activityName, description, price);
        activity = activityFinder.findActivityByName(activityName).get();

    }

    @When("he adds this activity to his cart for the date {string}")
    public void heAddsTheActivityForTheDateToHisCart(String date) throws Throwable {
        TimeSlotItem timeSlotItem = cartModifier.timeSlotUpdate(customer.getId(), activity, date);
        customer.getCaddy().getCatalogItem().add(timeSlotItem);
        assertEquals(1, customer.getCaddy().getCatalogItem().size());
    }


    @And("^he proceeds to checkout$")
    public void heProceedsToCheckout() throws EmptyCartException, NegativeAmountTransactionException, PaymentException, IdNotFoundException {
        Item item = customer.getCaddy().getCatalogItem().iterator().next();
        Reservation reservation = cartProcessor.validateActivity(customer.getId(), item);
        customer.getCaddy().getCatalogItem().remove(item);
        Transaction transaction = reservation.getTransaction();
        transactionRepository.save(transaction);
        reservationRepository.save(reservation);
        assertTrue(customer.getCaddy().getCatalogItem().isEmpty());
    }

    @Then("a transaction of {double} should appear on the Transaction History of {string}")
    public void aTransactionOfShouldAppearOnTheTransactionHistoryOf(double transactionAmount, String customerName) {
        assertEquals(customerName, customer.getName());
        assertEquals(1, transactionFinder.findTransactionsByCustomer(customer.getId()).size());
        Transaction transaction = transactionFinder.findTransactionsByCustomer(customer.getId()).get(0);
        assertEquals(transactionAmount, transaction.getAmount());
    }

    @And("the Activity {string} should be reserved")
    public void theActivityShouldBeReserved(String activityName) {
        assertEquals(activityName, activity.getName());
        Reservation reservation = reservationFinder.findTimeSlotReservationByCard(customer.getCard().getId(), ReservationType.TIME_SLOT).get(0);
        assertNotNull(reservation);
        assertEquals(customer.getId(), reservation.getCard().getId());
    }
}
