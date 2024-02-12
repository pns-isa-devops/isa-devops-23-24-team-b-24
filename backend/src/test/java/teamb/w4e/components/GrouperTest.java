package teamb.w4e.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.AlreadyExistingCustomerException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughMembersException;
import teamb.w4e.exceptions.group.SameMemberInGroupException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.CustomerRegistration;

import java.util.List;

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

        Group newGroup = grouper.createGroup(johnCustomer, List.of(janeCustomer));

        assertEquals(johnCustomer, newGroup.getLeader());
        assertEquals(List.of(janeCustomer), newGroup.getMembers());
        assertNotNull(newGroup);
    }

    @Test
    void leaderIsMember() throws AlreadyExistingCustomerException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);

        Assertions.assertThrows(AlreadyLeaderException.class, () -> grouper.createGroup(johnCustomer, List.of(janeCustomer, johnCustomer)));
    }

    @Test
    void alreadyLeader() throws AlreadyExistingCustomerException, AlreadyLeaderException, NotEnoughMembersException, SameMemberInGroupException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);
        Customer mattCustomer = customerRegistration.register(matt, creditCard);

        grouper.createGroup(johnCustomer, List.of(janeCustomer, mattCustomer));
        Assertions.assertThrows(AlreadyLeaderException.class, () -> grouper.createGroup(johnCustomer, List.of(janeCustomer)));
    }

    @Test
    void notEnoughMembers() throws AlreadyExistingCustomerException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Assertions.assertThrows(NotEnoughMembersException.class, () -> grouper.createGroup(johnCustomer, List.of()));
    }

    @Test
    void TwiceSameMembers() throws AlreadyExistingCustomerException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);

        Assertions.assertThrows(SameMemberInGroupException.class, () -> grouper.createGroup(johnCustomer, List.of(janeCustomer, janeCustomer)));
    }

    @Test
    void findGroupByLeader() throws AlreadyExistingCustomerException, AlreadyLeaderException, NotEnoughMembersException, SameMemberInGroupException {
        Customer johnCustomer = customerRegistration.register(john, creditCard);
        Customer janeCustomer = customerRegistration.register(jane, creditCard);
        Customer mattCustomer = customerRegistration.register(matt, creditCard);

        Group newGroup = grouper.createGroup(johnCustomer, List.of(janeCustomer, mattCustomer));
        assertEquals(newGroup, grouper.findGroupByLeader(johnCustomer).get());
    }

}
