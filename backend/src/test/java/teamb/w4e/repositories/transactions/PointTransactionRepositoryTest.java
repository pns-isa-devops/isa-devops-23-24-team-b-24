package teamb.w4e.repositories.transactions;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.repositories.customers.CustomerRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PointTransactionRepositoryTest {
    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testIdGeneration() {
        Customer john = new Customer("john", "1234567890");
        customerRepository.saveAndFlush(john);
        PointTransaction pointTransaction = new PointTransaction(john, 123, "1234567890");
        assertNull(pointTransaction.getId());
        pointTransactionRepository.saveAndFlush(pointTransaction);
        assertNotNull(pointTransaction.getId());
        assertNotNull(pointTransaction.getCustomer());
    }

    @Test
    void testTransactionWithoutCustomer() {
        PointTransaction pointTransaction = new PointTransaction(null, 123, "1234567890");
        assertNull(pointTransaction.getId());
        assertThrows(ConstraintViolationException.class, () -> pointTransactionRepository.saveAndFlush(pointTransaction));
    }

    @Test
    void testTransactionWithNegativePoints() {
        Customer john = new Customer("john", "1234567890");
        customerRepository.saveAndFlush(john);
        PointTransaction pointTransaction = new PointTransaction(john, -123, "1234567890");
        assertNull(pointTransaction.getId());
        assertThrows(ConstraintViolationException.class, () -> pointTransactionRepository.saveAndFlush(pointTransaction));
    }

    @Test
    void testTransactionWithBlankReceiptId() {
        Customer john = new Customer("john", "1234567890");
        customerRepository.saveAndFlush(john);
        PointTransaction pointTransaction = new PointTransaction(john, 123, "");
        assertNull(pointTransaction.getId());
        assertThrows(ConstraintViolationException.class, () -> pointTransactionRepository.saveAndFlush(pointTransaction));
        PointTransaction pointTransaction2 = new PointTransaction(john, 123, " ");
        assertNull(pointTransaction2.getId());
        assertThrows(ConstraintViolationException.class, () -> pointTransactionRepository.saveAndFlush(pointTransaction2));
    }

    @Test
    void testFindPointTransactionByCustomer() {
        Customer john = new Customer("john", "1234567890");
        customerRepository.saveAndFlush(john);
        PointTransaction pointTransaction = new PointTransaction(john, 123, "1234567890");
        pointTransactionRepository.saveAndFlush(pointTransaction);
        assertEquals(pointTransactionRepository.findPointTransactionByCustomer(john.getId()).get(), pointTransaction);
    }

}
