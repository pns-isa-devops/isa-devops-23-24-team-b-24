package teamb.w4e.repositories;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {
    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final Activity activity = new Activity("activity", "desc", 123, Set.of());
    private final Customer customer = new Customer("john", "1234567890");
    @BeforeEach
    void setUp() {
        customerRepository.saveAndFlush(customer);
        activityCatalogRepository.saveAndFlush(activity);
    }

    @Test
    void testIdGenerationAndUnicity() {
        Transaction transaction = new Transaction(customer, 123, "1234567890");
        assertNull(transaction.getId());
        transactionRepository.saveAndFlush(transaction); // save in the persistent context and force saving in the DB (thus ensuring validation by Hibernate)
        assertNotNull(transaction.getId());
        assertNotNull(transaction.getCustomer());
    }

    @Test
    void testTransactionWithoutCustomer() {
        Customer customer = new Customer("john", "1234567890");
        Transaction transaction = new Transaction(customer, 123, "1234567890");
        assertNull(transaction.getId());
        assertThrows(InvalidDataAccessApiUsageException.class, () -> transactionRepository.saveAndFlush(transaction));
    }


  @Test
  void testFindTransactionByCustomer() {
    transactionRepository.saveAndFlush(new Transaction(customer, 123, "1234567890"));
    assertTrue(transactionRepository.findTransactionByCustomer(customer.getId()).isPresent());
  }

    @Test
    void testNotBlankPaymentId() {
        Transaction emptyPaymentId = new Transaction(customer, 123, "");
        Transaction blankPaymentId = new Transaction(customer, 123, "    ");
        assertThrows(ConstraintViolationException.class, () -> transactionRepository.saveAndFlush(emptyPaymentId));
        assertThrows(ConstraintViolationException.class, () -> transactionRepository.saveAndFlush(blankPaymentId));
    }

    @Test
    void testAmount() {
        Transaction zeroAmount = new Transaction(customer, 0, "1234567890");
        Transaction negativeAmount = new Transaction(customer, -1, "1234567890");
            assertThrows(ConstraintViolationException.class, () -> transactionRepository.saveAndFlush(zeroAmount));
        assertThrows(ConstraintViolationException.class, () -> transactionRepository.saveAndFlush(negativeAmount));
    }
}
