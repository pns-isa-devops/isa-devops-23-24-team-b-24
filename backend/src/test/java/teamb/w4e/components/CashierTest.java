package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.PartnerRepository;
import teamb.w4e.repositories.TransactionRepository;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;

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

    @Autowired
    private PartnerRepository partnerRepository;

    @MockBean
    private Bank bankProxy;

    // the 896983 is the magic key for the bank
    private Customer customer;
    private Partner partner;

    @BeforeEach
    void setUpContext() {
        partner = new Partner("partner");
        partnerRepository.save(partner);
        customer = new Customer("John", "5251896983");
        customerRepository.save(customer);
        when(bankProxy.pay(customer, 100.0)).thenReturn(Optional.of("playReceiptOKId"));
    }

    @Test
    void payRegularAmount() throws Exception {
        customerRepository.save(customer);
        Activity activity = new Activity(partner, "a", "a", 100, Set.of());
        activityCatalogRepository.save(activity);
        Item item = new TimeSlotItem(activity, "01-10 23:56");
        payment.payReservationFromCart(customer, item);
        Transaction transaction = transactionRepository.findTransactionByCustomer(customer.getId()).orElse(null);
        assertEquals(customer, transaction.getCustomer());
        Customer ed = customerRepository.findById(this.customer.getId()).orElse(customer);
        assertEquals(ed.getCard().getPoints(), 100 * 0.9);
        assertEquals(100, transaction.getAmount());
        assertNotNull(transaction.getPaymentId());
        assertEquals(transaction, transactionRepository.findById(transaction.getId()).get());
    }

    @Test
    void payNegativeAmount() {
        customerRepository.save(customer);
        Item item = new TimeSlotItem(new Activity(partner, "a", "a", -12, Set.of()), "01-10 23:56");
        assertThrows(NegativeAmountTransactionException.class, () -> payment.payReservationFromCart(customer, item));
        assertNull(transactionRepository.findTransactionByCustomer(customer.getId()).orElse(null));
    }
}
