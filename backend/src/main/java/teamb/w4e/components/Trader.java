package teamb.w4e.components;

import org.springframework.stereotype.Service;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.group.NotEnoughException;
import teamb.w4e.interfaces.GroupFinder;
import teamb.w4e.interfaces.PointTransactionCreator;
import teamb.w4e.interfaces.TradeCreator;

import java.util.List;

@Service
public class Trader implements TradeCreator {
    private final PointTransactionCreator pointTransactionCreator;
    private final GroupFinder groupFinder;

    public Trader(PointTransactionCreator pointTransactionCreator, GroupFinder groupFinder) {
        this.pointTransactionCreator = pointTransactionCreator;
        this.groupFinder = groupFinder;
    }

    @Override
    public PointTransaction createTrade(Customer sender, Customer receiver, int points) throws NotEnoughException {
        if (sender.getCard().getPoints() < points) {
            throw new NotEnoughException("Not enough points to trade");
        }
        if (areInSameGroup(sender, receiver)) {
            sender.getCard().setPoints(sender.getCard().getPoints() - points);
            receiver.getCard().setPoints(receiver.getCard().getPoints() + points);
        } else {
            sender.getCard().setPoints(sender.getCard().getPoints() - points);
            receiver.getCard().setPoints(receiver.getCard().getPoints() + (int) (points * 0.5));
        }
        return pointTransactionCreator.createPointTransaction(sender, receiver, points);
    }

    @Override
    public boolean areInSameGroup(Customer sender, Customer receiver) {
        List<Group> groups = groupFinder.findAll();
        return groups.stream()
                .anyMatch(group -> {
                    boolean isSenderInGroup = group.getLeader().equals(sender) || group.getMembers().contains(sender);
                    boolean isReceiverInGroup = group.getLeader().equals(receiver) || group.getMembers().contains(receiver);
                    return isSenderInGroup && isReceiverInGroup;
                });
    }
}
