package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.CustomerIdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface GroupFinder {
    Optional<Group> findGroupByLeader(Long leaderId);

    Group retrieveGroup(Long leaderId) throws CustomerIdNotFoundException;

    List<Group> findAll();
}
