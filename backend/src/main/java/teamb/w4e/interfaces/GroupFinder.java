package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;

import java.util.List;
import java.util.Optional;

public interface GroupFinder {
    Optional<Group> findGroupByLeader(Customer leader);

    List<Group> findAll();
}
