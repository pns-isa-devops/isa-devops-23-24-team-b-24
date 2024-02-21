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

    private final String payReceiptId = "123456789";
    private final double amount = 100.0;

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
        assertFalse(transactionFinder.findAllTransactions().isEmpty());

    }

    @Test
    void findTransactionsByCustomer() {
    }

    @Test
    void createTransaction() {
    }
}
