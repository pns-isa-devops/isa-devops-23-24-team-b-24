package teamb.w4e.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.exceptions.group.NotEnoughException;
import teamb.w4e.interfaces.TransactionFinder;
import teamb.w4e.repositories.customers.CustomerRepository;
import teamb.w4e.repositories.customers.GroupRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TraderTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionFinder transactionFinder;

    @Autowired
    private Trader trader;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void pointTradeOutOfGroup() throws NotEnoughException {
        Customer sender = new Customer("John", "1234567890");
        Customer receiver = new Customer("Jane", "0987654321");
        customerRepository.save(sender);
        customerRepository.save(receiver);
        sender.getCard().setPoints(100);
        trader.createTrade(sender, receiver, 60);
        assertEquals(40, sender.getCard().getPoints());
        assertEquals(30, receiver.getCard().getPoints());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(sender.getId()).size());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(receiver.getId()).size());
    }

    @Test
    void pointTradeInGroup() throws NotEnoughException {
        Customer sender = new Customer("John", "1234567890");
        Customer receiver = new Customer("Jane", "0987654321");
        customerRepository.save(sender);
        customerRepository.save(receiver);
        groupRepository.save(new Group(sender, Set.of(receiver)));
        sender.getCard().setPoints(100);
        trader.createTrade(sender, receiver, 60);
        assertEquals(40, sender.getCard().getPoints());
        assertEquals(60, receiver.getCard().getPoints());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(sender.getId()).size());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(receiver.getId()).size());
    }

}
