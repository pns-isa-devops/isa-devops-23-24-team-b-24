package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.exceptions.PaymentException;

public interface Payment {

    boolean pay(Customer customer) throws PaymentException;

}
