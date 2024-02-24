package teamb.w4e.interfaces;

import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.*;

public interface CartProcessor {

    Reservation validate(Long customerId, Item item, ReservationType type) throws IdNotFoundException, EmptyCartException, PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException;
}
