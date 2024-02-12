package teamb.w4e.interfaces;

import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughMembersException;

import java.util.List;
import java.util.Optional;

public interface GroupCreator {
    Group createGroup(Customer leader, List<Customer> members) throws NotEnoughMembersException, AlreadyLeaderException;

    Optional<Group> findGroupByLeader(Customer leader);

    List<Group> findAll();
}
