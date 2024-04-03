package teamb.w4e.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughException;
import teamb.w4e.exceptions.group.NotInTheSameGroupException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.CustomerRegistration;
import teamb.w4e.interfaces.TransactionFinder;
import teamb.w4e.repositories.customers.CustomerRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // default behavior : rollback DB operations after each test (even if it fails)
class GrouperTest {

    @Autowired
    private Grouper grouper;

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerFinder customerFinder;

    @Autowired
    private TransactionFinder transactionFinder;

    private final String john = "John";
    private final String jane = "Jane";
    private final String matt = "Matt";
    private final String creditCard = "1234567890";

    @Test
    void createGroup() throws Exception {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);

        Group newGroup = grouper.createGroup(johnCustomer.getId(), Set.of(janeCustomer.getId()));

        assertEquals(johnCustomer, newGroup.getLeader());
        assertEquals(Set.of(janeCustomer), newGroup.getMembers());
        assertNotNull(newGroup);
    }

    @Test
    void leaderIsMember() throws AlreadyExistingException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);

        assertThrows(AlreadyLeaderException.class, () -> grouper.createGroup(johnCustomer.getId(), Set.of(janeCustomer.getId(), johnCustomer.getId())));
    }

    @Test
    void alreadyLeader() throws AlreadyExistingException, AlreadyLeaderException, NotEnoughException, IdNotFoundException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);
        Customer mattCustomer = customerRegistration.register(matt, creditCard);

        grouper.createGroup(johnCustomer.getId(), Set.of(janeCustomer.getId(), mattCustomer.getId()));
        assertThrows(AlreadyLeaderException.class, () -> grouper.createGroup(johnCustomer.getId(), Set.of(janeCustomer.getId())));
    }

    @Test
    void notEnoughMembers() throws AlreadyExistingException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        assertThrows(NotEnoughException.class, () -> grouper.createGroup(johnCustomer.getId(), Set.of()));
    }

    @Test
    void findGroupByLeader() throws AlreadyExistingException, AlreadyLeaderException, NotEnoughException, IdNotFoundException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);
        Customer mattCustomer = customerRegistration.register(matt, creditCard);
        assertEquals(customerFinder.findById(johnCustomer.getId()).get(), johnCustomer);

        Group newGroup = grouper.createGroup(johnCustomer.getId(), Set.of(janeCustomer.getId(), mattCustomer.getId()));
        assertEquals(newGroup, grouper.findGroupByLeader(johnCustomer.getId()).get());
    }

    @Test
    void pointTradeOutOfGroup() throws NotEnoughException, IdNotFoundException, NotInTheSameGroupException {
        Customer sender = new Customer("John", "1234567890");
        Customer receiver = new Customer("Jane", "0987654321");
        customerRepository.save(sender);
        customerRepository.save(receiver);
        sender.getCard().setPoints(100);
        assertThrows(NotInTheSameGroupException.class, () -> grouper.createTrade(sender.getId(), receiver.getId(), 60));
        assertEquals(100, sender.getCard().getPoints()); // no points were traded
        assertEquals(0, transactionFinder.findPointTransactionsByCustomer(sender.getId()).size());
    }

    @Test
    void pointTradeInGroup() throws NotEnoughException, AlreadyLeaderException, IdNotFoundException, NotInTheSameGroupException {
        Customer sender = new Customer("John", "1234567890");
        Customer receiver = new Customer("Jane", "0987654321");
        customerRepository.save(sender);
        customerRepository.save(receiver);
        grouper.createGroup(sender.getId(), Set.of(receiver.getId()));
        sender.getCard().setPoints(100);
        grouper.createTrade(sender.getId(), receiver.getId(), 60);
        assertEquals(40, sender.getCard().getPoints());
        assertEquals(60, receiver.getCard().getPoints());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(sender.getId()).size());
        assertEquals(1, transactionFinder.findPointTransactionsByCustomer(receiver.getId()).size());
    }

}
