package teamb.w4e.interfaces;

import teamb.w4e.entities.Order;

public interface OrderModifier {

    Order orderIsNowInProgress(Order order);

    Order orderIsNowReady(Order order);
}
