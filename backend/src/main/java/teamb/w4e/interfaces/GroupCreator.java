package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughMembersException;
import teamb.w4e.exceptions.group.SameMemberInGroupException;

import java.util.List;

public interface GroupCreator {
    Group createGroup(Customer leader, List<Customer> members) throws NotEnoughMembersException, AlreadyLeaderException, SameMemberInGroupException;
}
