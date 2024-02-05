package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Order;


public interface OrderCreator {

        Order createOrder(Customer customer, double price, String payReceiptId);

}
