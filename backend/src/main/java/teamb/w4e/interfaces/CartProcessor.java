package teamb.w4e.interfaces;

import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.exceptions.*;

public interface CartProcessor {

    Reservation validate(Long customerId, Item item) throws IdNotFoundException, EmptyCartException, PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException;
}
