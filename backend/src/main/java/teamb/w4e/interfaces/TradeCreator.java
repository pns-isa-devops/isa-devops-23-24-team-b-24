package teamb.w4e.interfaces;

import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.group.NotEnoughException;

public interface TradeCreator {
    PointTransaction createTrade(Customer sender, Customer receiver, int points) throws NotEnoughException;

    boolean areInSameGroup(Customer sender, Customer receiver);
}
