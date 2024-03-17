package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughMembersException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.GroupCreator;
import teamb.w4e.interfaces.GroupFinder;
import teamb.w4e.repositories.GroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class Grouper implements GroupCreator, GroupFinder {

    private final GroupRepository groupRepository;
    private final CustomerFinder customerFinder;

    @Autowired
    public Grouper(GroupRepository groupRepository, CustomerFinder customerFinder) {
        this.groupRepository = groupRepository;
        this.customerFinder = customerFinder;
    }

    @Override
    @Transactional
    public Group createGroup(Customer leader, Set<Customer> members) throws NotEnoughMembersException, AlreadyLeaderException, IdNotFoundException {
        if (groupRepository.findGroupByLeader(leader.getId()).isPresent()) {
            throw new AlreadyLeaderException(leader + " is already a leader");
        }
        if (members.contains(leader)) {
            throw new AlreadyLeaderException("This member is already a leader");
        }
        if (members.isEmpty()) {
            throw new NotEnoughMembersException("There must be at least one member in the group");
        }

        if(customerFinder.findByName(leader.getName()).isEmpty()) {
            throw new IdNotFoundException(leader.getId());
        }
        for (Customer member : members) {
            if(customerFinder.findByName(member.getName()).isEmpty()) {
                throw new IdNotFoundException(member.getId());
            }
        }
        Group newGroup = new Group(leader, members);
        return groupRepository.save(newGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Group> findGroupByLeader(Long leaderId) {
        return groupRepository.findGroupByLeader(leaderId);
    }

    @Override
    @Transactional(readOnly = true)
    public Group retrieveGroup(Long leaderId) throws IdNotFoundException {
        return findGroupByLeader(leaderId).orElseThrow(() -> new IdNotFoundException(leaderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findAll() {
        return groupRepository.findAll();
    }
}
