package teamb.w4e.repositories.customers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testIdGenerationAndUnicity() {
        Customer john = new Customer("john", "1234567890");
        customerRepository.saveAndFlush(john);
        Customer jane = new Customer("jane", "1234567891");
        customerRepository.saveAndFlush(jane);
        Group group = new Group(john, Set.of(jane));
        assertNull(group.getId());
        groupRepository.saveAndFlush(group); // save in the persistent context and force saving in the DB (thus ensuring validation by Hibernate)
        assertNotNull(group.getId());
        assertNotNull(group.getMembers());
        assertThrows(DataIntegrityViolationException.class, () -> groupRepository.saveAndFlush(new Group(john, Set.of(jane))));
    }

    @Test
    void testFindGroupByCustomer() {
        Customer john = new Customer("john", "1234567890");
        customerRepository.saveAndFlush(john);
        Customer jane = new Customer("jane", "1234567891");
        customerRepository.saveAndFlush(jane);
        Group group = new Group(john, Set.of(jane));
        groupRepository.saveAndFlush(group);
        assertEquals(groupRepository.findGroupByLeader(john.getId()).get(), group);
    }
}
