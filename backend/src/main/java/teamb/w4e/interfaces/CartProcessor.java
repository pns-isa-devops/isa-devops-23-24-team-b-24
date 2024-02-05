package teamb.w4e.interfaces;

import teamb.w4e.entities.Order;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.EmptyCartException;
import teamb.w4e.exceptions.PaymentException;

public interface CartProcessor {

    double cartPrice(Long customerId) throws CustomerIdNotFoundException;

    Order validate(Long customerId) throws PaymentException, EmptyCartException, CustomerIdNotFoundException;

}
