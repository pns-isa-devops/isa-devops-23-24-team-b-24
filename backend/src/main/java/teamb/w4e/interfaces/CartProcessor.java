package teamb.w4e.interfaces;

import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.ServiceItem;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.exceptions.*;

public interface CartProcessor {

    Reservation validateActivity(Long customerId, Item item) throws EmptyCartException, PaymentException, IdNotFoundException, NegativeAmountTransactionException, CannotReserveException;
    Transaction validateService(Long customerId, ServiceItem item) throws EmptyCartException, PaymentException, IdNotFoundException, NegativeAmountTransactionException;
}
