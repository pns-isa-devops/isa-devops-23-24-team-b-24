package teamb.w4e.interfaces;

import teamb.w4e.entities.Reservation;
import teamb.w4e.exceptions.EmptyCartException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.PaymentException;

import java.util.Set;

public interface CartProcessor {

    Set<Reservation> validate(Long customerId) throws IdNotFoundException, EmptyCartException, PaymentException;

}
