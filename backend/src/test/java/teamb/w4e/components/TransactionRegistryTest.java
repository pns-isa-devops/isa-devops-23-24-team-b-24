package teamb.w4e.components;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.PointTransaction;
import teamb.w4e.interfaces.GroupFinder;
import teamb.w4e.interfaces.TransactionCreator;
import teamb.w4e.interfaces.TransactionFinder;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.GroupRepository;
import teamb.w4e.repositories.PointTransactionRepository;
import teamb.w4e.repositories.TransactionRepository;

import java.util.List;
import java.util.Set;

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
    private TransactionRegistry transactionRegistry;
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupFinder groupFinder;

    private final String payReceiptId = "123456789";
    private final double amount = 100.0;

    private final String payReceiptId2 = "234567891";
    private final double amount2 = 200.0;

    boolean areInSameGroup(Customer sender, Customer receiver) {
        List<Group> groups = groupFinder.findAll();
        return groups.stream()
                .anyMatch(group -> {
                    boolean isSenderInGroup = group.getLeader().equals(sender) || group.getMembers().contains(sender);
                    boolean isReceiverInGroup = group.getLeader().equals(receiver) || group.getMembers().contains(receiver);
                    return isSenderInGroup && isReceiverInGroup;
                });
    }

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

    @Test
    void pointTradeOutOfGroup() {
        Customer sender = new Customer("John", "1234567890");
        Customer receiver = new Customer("Jane", "0987654321");
        customerRepository.save(sender);
        customerRepository.save(receiver);
        sender.getCard().setPoints(100);
        transactionRegistry.createTrade(sender, receiver, 60, areInSameGroup(sender, receiver));
        assertEquals(40, sender.getCard().getPoints());
        assertEquals(30, receiver.getCard().getPoints());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(sender.getId()).size());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(receiver.getId()).size());
    }

    @Test
    void pointTradeInGroup() {
        Customer sender = new Customer("John", "1234567890");
        Customer receiver = new Customer("Jane", "0987654321");
        customerRepository.save(sender);
        customerRepository.save(receiver);
        groupRepository.save(new Group(sender, Set.of(receiver)));
        sender.getCard().setPoints(100);
        transactionRegistry.createTrade(sender, receiver, 60, areInSameGroup(sender, receiver));
        assertEquals(40, sender.getCard().getPoints());
        assertEquals(60, receiver.getCard().getPoints());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(sender.getId()).size());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(receiver.getId()).size());
    }
}
