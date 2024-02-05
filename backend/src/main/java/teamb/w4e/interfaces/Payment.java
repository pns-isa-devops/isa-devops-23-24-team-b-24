package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Order;
import teamb.w4e.exceptions.PaymentException;

public interface Payment {

    Order payOrderFromCart(Customer customer, double price) throws PaymentException;

}
