package teamb.w4e.components;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.*;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.repositories.ActivityCatalogRepository;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.TransactionRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class CashierTest {

    @Autowired
    private Payment payment;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;

    @MockBean
    private Bank bankProxy;

    // the 896983 is the magic key for the bank
    private Customer customer;
    @BeforeEach
    void setUpContext() {
        customer = new Customer("John", "5251896983");
        customerRepository.save(customer);
        when(bankProxy.pay(customer, 100.0)).thenReturn(Optional.of("playReceiptOKId"));
    }

    @Test
    void payRegularAmount() throws Exception {
        customerRepository.save(customer);
        Activity activity = new Activity("a", "a", 100, Set.of());
        activityCatalogRepository.save(activity);
        Item item = new Item(activity, "01-10 23:56");
        payment.payReservationFromCart(customer, item, ReservationType.NONE);
        Transaction transaction = transactionRepository.findTransactionByCustomer(customer.getId()).orElse(null);
        assertEquals(customer, transaction.getCustomer());
        assertEquals(100, transaction.getAmount());
        assertNotNull(transaction.getPaymentId());
        assertEquals(transaction, transactionRepository.findById(transaction.getId()).get());
    }

    @Test
    void payNegativeAmount() {
        customerRepository.save(customer);
        Item item = new Item(new Activity("a", "a", -12, Set.of()), "01-10 23:56");
        assertThrows(NegativeAmountTransactionException.class, () -> payment.payReservationFromCart(customer, item, ReservationType.NONE));
        assertNull(transactionRepository.findTransactionByCustomer(customer.getId()).orElse(null));
    }
}
