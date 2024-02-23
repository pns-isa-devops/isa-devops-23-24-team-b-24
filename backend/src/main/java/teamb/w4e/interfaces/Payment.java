package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
import teamb.w4e.exceptions.PaymentException;

public interface Payment {

    Reservation payReservationFromCart(Customer customer, Item item) throws PaymentException, NegativeAmountTransactionException;
  
}
