package teamb.w4e.interfaces;

import teamb.w4e.entities.customers.Group;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface GroupFinder {
    Optional<Group> findGroupByLeader(Long leaderId);

    Group retrieveGroup(Long leaderId) throws IdNotFoundException;

    List<Group> findAll();
}
