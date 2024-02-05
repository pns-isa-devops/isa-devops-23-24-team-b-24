package teamb.w4e.interfaces;

import teamb.w4e.entities.Order;
import teamb.w4e.entities.OrderStatus;
import teamb.w4e.exceptions.OrderIdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface OrderFinder {

    Optional<Order> findById(Long id);

    List<Order> findAll();

    Order retrieveOrder(Long orderId) throws OrderIdNotFoundException;

    OrderStatus retrieveOrderStatus(Long orderId) throws OrderIdNotFoundException;

}