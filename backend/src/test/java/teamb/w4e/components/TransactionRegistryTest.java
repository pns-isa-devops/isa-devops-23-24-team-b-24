package teamb.w4e.components;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.interfaces.TransactionCreator;
import teamb.w4e.interfaces.TransactionFinder;
import teamb.w4e.repositories.CustomerRepository;
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

    private final String payReceiptId = "123456789";
    private final double amount = 100.0;

    private final String payReceiptId2 = "234567891";
    private final double amount2 = 200.0;

    @Test
    void unknownTransaction() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        assertFalse(transactionFinder.findTransactionByCustomer(customer).isPresent());
    }

    @Test
    void findTransactionByCustomer() {
        Customer customer = new Customer("John", "1234567890");
        customerRepository.save(customer);
        transactionCreator.createTransaction(customer, amount, payReceiptId);
        assertTrue(transactionFinder.findTransactionByCustomer(customer).isPresent());
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
        assertFalse(transactionFinder.findTransactionsByCustomer(customer).isEmpty());
        assertEquals(2, transactionFinder.findTransactionsByCustomer(customer).size());
        // delete all transactions
        transactionRepository.deleteAll(transactionFinder.findTransactionsByCustomer(customer));
        assertNull(transactionFinder.findTransactionByCustomer(customer).orElse(null));
    }
}
