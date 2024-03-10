package teamb.w4e.interfaces;

import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.ServiceItem;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.exceptions.*;

public interface CartProcessor {

    Reservation validateActivity(Long customerId, Item item) throws IdNotFoundException, EmptyCartException, PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException;
    Transaction validateService(Long customerId, ServiceItem item) throws EmptyCartException, PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException;
}
