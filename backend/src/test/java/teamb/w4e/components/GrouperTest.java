package teamb.w4e.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughMembersException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.CustomerRegistration;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional // default behavior : rollback DB operations after each test (even if it fails)
class GrouperTest {

    @Autowired
    private Grouper grouper;

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private CustomerFinder customerFinder;

    private final String john = "John";
    private final String jane = "Jane";
    private final String matt = "Matt";
    private final String creditCard = "1234567890";

    @Test
    void createGroup() throws Exception {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);

        Group newGroup = grouper.createGroup(johnCustomer, Set.of(janeCustomer));

        assertEquals(johnCustomer, newGroup.getLeader());
        assertEquals(Set.of(janeCustomer), newGroup.getMembers());
        assertNotNull(newGroup);
    }

    @Test
    void leaderIsMember() throws AlreadyExistingException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);

        Assertions.assertThrows(AlreadyLeaderException.class, () -> grouper.createGroup(johnCustomer, Set.of(janeCustomer, johnCustomer)));
    }

    @Test
    void alreadyLeader() throws AlreadyExistingException, AlreadyLeaderException, NotEnoughMembersException, IdNotFoundException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);
        Customer mattCustomer = customerRegistration.register(matt, creditCard);

        grouper.createGroup(johnCustomer, Set.of(janeCustomer, mattCustomer));
        Assertions.assertThrows(AlreadyLeaderException.class, () -> grouper.createGroup(johnCustomer, Set.of(janeCustomer)));
    }

    @Test
    void notEnoughMembers() throws AlreadyExistingException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Assertions.assertThrows(NotEnoughMembersException.class, () -> grouper.createGroup(johnCustomer, Set.of()));
    }

    @Test
    void findGroupByLeader() throws AlreadyExistingException, AlreadyLeaderException, NotEnoughMembersException, IdNotFoundException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);
        Customer mattCustomer = customerRegistration.register(matt, creditCard);
        assertEquals(customerFinder.findById(johnCustomer.getId()).get(), johnCustomer);

        Group newGroup = grouper.createGroup(johnCustomer, Set.of(janeCustomer, mattCustomer));
        assertEquals(newGroup, grouper.findGroupByLeader(johnCustomer.getId()).get());
    }

}
