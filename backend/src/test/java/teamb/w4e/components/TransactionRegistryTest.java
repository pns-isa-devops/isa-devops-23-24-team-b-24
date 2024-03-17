package teamb.w4e.components;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.PointTransaction;
import teamb.w4e.interfaces.TransactionCreator;
import teamb.w4e.interfaces.TransactionFinder;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.PointTransactionRepository;
import teamb.w4e.repositories.TransactionRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionRegistryTest {

    @Autowired
    private TransactionFinder transactionFinder;

    @Autowired
    private TransactionCreator transactionCreator;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    private final String payReceiptId = "123456789";
    private final double amount = 100.0;

    private final String payReceiptId2 = "234567891";
    private final double amount2 = 200.0;

    @Test
    void unknownTransaction() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        assertFalse(transactionFinder.findTransactionByCustomer(customer.getId()).isPresent());
    }

    @Test
    void findTransactionByCustomer() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        transactionCreator.createTransaction(customer, amount, payReceiptId);
        assertTrue(transactionFinder.findTransactionByCustomer(customer.getId()).isPresent());
    }

    @Test
    void findTransactionById() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        long id = transactionCreator.createTransaction(customer, amount, payReceiptId).getId();
        assertTrue(transactionFinder.findTransactionById(id).isPresent());
    }

    @Test
    void findAllTransactions() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        transactionCreator.createTransaction(customer, amount, payReceiptId);
        transactionCreator.createTransaction(customer, amount2, payReceiptId2);
        assertFalse(transactionFinder.findAllTransactions().isEmpty());
        assertEquals(2, transactionFinder.findAllTransactions().size());
        // delete all transactions
        transactionRepository.deleteAll(transactionFinder.findAllTransactions());
        assertTrue(transactionFinder.findAllTransactions().isEmpty());
    }

    @Test
    void findTransactionsByCustomer() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        transactionCreator.createTransaction(customer, amount, payReceiptId);
        transactionCreator.createTransaction(customer, amount2, payReceiptId2);
        assertFalse(transactionFinder.findTransactionsByCustomer(customer.getId()).isEmpty());
        assertEquals(2, transactionFinder.findTransactionsByCustomer(customer.getId()).size());
        // delete all transactions
        transactionRepository.deleteAll(transactionFinder.findTransactionsByCustomer(customer.getId()));
        assertNull(transactionFinder.findTransactionByCustomer(customer.getId()).orElse(null));
    }

    // Same tests as above, but for PointTransactions
    @Test
    void findPointTransactionsByCustomer() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        transactionCreator.createPointTransaction(customer, (int) amount, null);
        transactionCreator.createPointTransaction(customer, (int) amount2, null);
        assertEquals(2, transactionFinder.findPointTransactionsByCustomer(customer.getId()).size());
        // delete all transactions
        pointTransactionRepository.deleteAll(transactionFinder.findPointTransactionsByCustomer(customer.getId()));
        assertNull(transactionFinder.findPointTransactionByCustomer(customer.getId()).orElse(null));
    }
}
