package teamb.w4e.interfaces;

import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughException;

import java.util.Set;

public interface GroupCreator {
    Group createGroup(Long leaderId, Set<Long> membersId) throws NotEnoughException, AlreadyLeaderException, IdNotFoundException;

    String deleteGroup(Long id) throws IdNotFoundException;

    PointTransaction createTrade(Long senderId, Long receiverId, int points) throws NotEnoughException, IdNotFoundException;

    boolean areInSameGroup(Customer sender, Customer receiver);


}
