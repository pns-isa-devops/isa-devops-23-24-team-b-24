package teamb.w4e.cucumber.reservation;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.reservations.TimeSlotReservation;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.*;
import teamb.w4e.interfaces.leisure.ActivityRegistration;
import teamb.w4e.interfaces.reservation.ReservationFinder;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.TransactionRepository;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.reservation.ReservationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Transactional
public class ReserveActivity {

    // We autowire the mock here because there are some bugs with the cucumber/spring integration
    @MockBean
    private Bank bankMock;


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private ActivityRegistration activityRegistration;

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

    private Activity activity;
    private String timeSlot;
    private Transaction transaction;

    @Before
    public void settingUpContext() {
        customerRepository.deleteAll();
        transactionRepository.deleteAll();
        reservationRepository.deleteAll();
        activityRepository.deleteAll();
    }
    

    @Given("^a customer named \"([^\"]*)\" with the credit card number \"([^\"]*)\"$")
    public void aCustomerNamedWithTheCreditCardNumber(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        customerRegistration.register(arg0, arg1);
    }

    @And("^a Activity named \"([^\"]*)\" with the description \"([^\"]*)\" and the price (\\d+) without advantages$")
    public void aActivityNamedWithTheDescriptionAndThePriceWithoutAdvantages(String arg0, String arg1, int arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        activityRegistration.register(arg0, arg1, arg2, null);
    }

    // Dans votre test
    @When("^he adds the Activity \"([^\"]*)\" for the date \"([^\"]*)\" to his cart$")
    public void heAddsTheActivityForTheDateToHisCart(String arg0, String arg1) throws Throwable {
        cartModifier = Mockito.mock(CartModifier.class);
        timeSlot = arg1;
        Customer customer = customerRepository.findCustomerByName("John").orElse(null);
        activity = activityRepository.findActivityByName(arg0).orElse(null);
        assertNotNull(customer);
        assertNotNull(activity);
        when(cartModifier.timeSlotUpdate(anyLong(), any(Activity.class), anyString())).thenReturn(new TimeSlotItem(activity, arg1));

        TimeSlotItem result = cartModifier.timeSlotUpdate(customer.getId(), activity, arg1);
        if (result != null) {
            customer.getCaddy().getLeisure().add(result);
        }
        assertEquals(1, customer.getCaddy().getLeisure().size());
    }


    @And("^he proceeds to checkout$")
    public void heProceedsToCheckout() throws EmptyCartException, NegativeAmountTransactionException, PaymentException, IdNotFoundException, CustomerIdNotFoundException {
        cartProcessor = Mockito.mock(CartProcessor.class);
        bankMock = Mockito.mock(Bank.class);
        Customer customer = customerRepository.findCustomerByName("John").orElse(null);
        assert customer != null;
        when(bankMock.pay(any(Customer.class), anyDouble())).thenReturn(Optional.of("playReceiptOKId"));
        when(cartProcessor.validateActivity(anyLong(), any(Item.class))).thenReturn(new TimeSlotReservation(activity,timeSlot, customer.getCard(), new Transaction(customer, activity.getPrice(), "playReceiptOKId")));
        // to simplify I take the first item in the cart
        Item item = customer.getCaddy().getLeisure().iterator().next();

        Reservation reservation = cartProcessor.validateActivity(customer.getId(), item);
        if (reservation != null) {
            customer.getCaddy().getLeisure().remove(item);
            transaction = reservation.getTransaction();
            transactionRepository.save(transaction);
            reservationRepository.save(reservation);
        }
        assertTrue(customer.getCaddy().getLeisure().isEmpty());
    }

    @Then("^a transaction of (\\d+) should appear on the Transaction History of \"([^\"]*)\"$")
    public void aTransactionOfShouldAppearOnTheTransactionHistoryOf(int arg0, String arg1) throws Throwable {
        Customer customer = customerRepository.findCustomerByName("John").orElse(null);
        assert customer != null;
        assertEquals(1, transactionFinder.findTransactionsByCustomer(customer.getId()).size());
        Transaction transaction = transactionFinder.findTransactionsByCustomer(customer.getId()).get(0);
        assertEquals(arg0, transaction.getAmount());
    }

    @And("^the Activity \"([^\"]*)\" should be reserved$")
    public void theActivityShouldBeReserved(String arg0) throws Throwable {
        Customer customer = customerRepository.findCustomerByName("John").orElse(null);
        Activity activity = activityRepository.findActivityByName(arg0).orElse(null);
        assert customer != null;
        assert activity != null;
        Reservation reservation = reservationFinder.findTimeSlotReservationByCard(customer.getCard().getId(), ReservationType.TIME_SLOT).get(0);
        assert reservation != null;
        assertEquals(customer.getId(), reservation.getCard().getId());
    }
}
