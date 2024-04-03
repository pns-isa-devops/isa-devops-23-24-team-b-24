package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughException;
import teamb.w4e.exceptions.group.NotInTheSameGroupException;
import teamb.w4e.interfaces.*;
import teamb.w4e.repositories.customers.GroupRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class Grouper implements GroupCreator, GroupFinder {

    private final GroupRepository groupRepository;
    private final CustomerFinder customerFinder;
    private final TransactionCreator transactionCreator;

    @Autowired
    public Grouper(GroupRepository groupRepository, CustomerFinder customerFinder, TransactionCreator transactionCreator) {
        this.groupRepository = groupRepository;
        this.customerFinder = customerFinder;
        this.transactionCreator = transactionCreator;
    }

    @Override
    @Transactional
    public Group createGroup(Long leaderId, Set<Long> membersId) throws NotEnoughException, AlreadyLeaderException, IdNotFoundException {
        Customer leader = customerFinder.findById(leaderId).orElseThrow(() -> new IdNotFoundException(leaderId));
        Set<Customer> members = new HashSet<>();
        for (Long memberId : membersId) {
            Customer member = customerFinder.findById(memberId).orElseThrow(() -> new IdNotFoundException(memberId));
            members.add(member);
        }
        if (groupRepository.findGroupByLeader(leader.getId()).isPresent()) {
            throw new AlreadyLeaderException(leader + " is already a leader");
        }
        if (members.contains(leader)) {
            throw new AlreadyLeaderException("This member is already a leader");
        }
        if (members.isEmpty()) {
            throw new NotEnoughException("There must be at least one member in the group");
        }

        if (customerFinder.findByName(leader.getName()).isEmpty()) {
            throw new IdNotFoundException(leader.getId());
        }
        for (Customer member : members) {
            if (customerFinder.findByName(member.getName()).isEmpty()) {
                throw new IdNotFoundException(member.getId());
            }
        }
        Group newGroup = new Group(leader, members);
        return groupRepository.save(newGroup);
    }

    @Override
    @Transactional
    public String deleteGroup(Long leaderId) throws IdNotFoundException {
        Group group = retrieveGroup(leaderId);
        groupRepository.delete(group);
        return "Group deleted";
    }

    @Override
    public PointTransaction createTrade(Long senderId, Long receiverId, int points) throws NotEnoughException, IdNotFoundException, NotInTheSameGroupException {
        Customer sender = customerFinder.findById(senderId).orElseThrow(() -> new IdNotFoundException(senderId));
        Customer receiver = customerFinder.findById(receiverId).orElseThrow(() -> new IdNotFoundException(receiverId));
        if (sender.getCard().getPoints() < points) {
            throw new NotEnoughException("Not enough points to trade");
        }
        if (areInSameGroup(sender, receiver)) {
            sender.getCard().setPoints(sender.getCard().getPoints() - points);
            receiver.getCard().setPoints(receiver.getCard().getPoints() + points);
            return transactionCreator.createPointTransaction(sender, receiver, points);
        }
        else {
            throw new NotInTheSameGroupException(sender.getName() + " and " + receiver.getName() + " are not in the same group");
        }
    }

    @Override
    public boolean areInSameGroup(Customer sender, Customer receiver) {
        List<Group> groups = groupRepository.findAll();
        return groups.stream()
                .anyMatch(group -> {
                    boolean isSenderInGroup = group.getLeader().equals(sender) || group.getMembers().contains(sender);
                    boolean isReceiverInGroup = group.getLeader().equals(receiver) || group.getMembers().contains(receiver);
                    return isSenderInGroup && isReceiverInGroup;
                });
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
