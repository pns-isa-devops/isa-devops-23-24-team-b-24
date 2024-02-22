package teamb.w4e.interfaces;

import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
import teamb.w4e.exceptions.EmptyCartException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.PaymentException;

public interface CartProcessor {

    Reservation validate(Long customerId, Item item) throws IdNotFoundException, EmptyCartException, PaymentException;
}
