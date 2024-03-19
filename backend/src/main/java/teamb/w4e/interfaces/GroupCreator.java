package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughException;

import java.util.Set;

public interface GroupCreator {
    Group createGroup(Customer leader, Set<Customer> members) throws NotEnoughException, AlreadyLeaderException, IdNotFoundException;

    String deleteGroup(Long id) throws IdNotFoundException;


}
