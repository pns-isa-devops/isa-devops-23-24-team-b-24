package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.PointTransaction;

public interface TradeCreator {
    PointTransaction createTrade(Customer sender, Customer receiver, int points, boolean inSameGroup);
}
