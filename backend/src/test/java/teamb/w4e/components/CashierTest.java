package teamb.w4e.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.TransactionRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CashierTest {

    @Autowired
    private Payment payment;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // the 896983 is the magic key for the bank
    private final Customer customer = new Customer("John", "1234567890896983");

    @Test
    void payRegularAmount() throws Exception {
        customerRepository.save(customer);
        double amount = 100.0;
        Transaction transaction = payment.pay(customer, amount);
        assertEquals(customer, transaction.getCustomer());
        assertEquals(amount, transaction.getAmount());
        assertNotNull(transaction.getPaymentId());
        assertEquals(transaction, transactionRepository.findById(transaction.getId()).get());
    }

    @Test
    void payNegativeAmount() {
        customerRepository.save(customer);
        double amount = -100.0;
        assertThrows(NegativeAmountTransactionException.class, () -> payment.pay(customer, amount));
        assertNull(transactionRepository.findTransactionByCustomer(customer).orElse(null));
    }
}
