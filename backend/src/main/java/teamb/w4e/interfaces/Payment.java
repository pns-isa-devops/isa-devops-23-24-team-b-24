package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.cart.ServiceItem;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.exceptions.PaymentException;

public interface Payment {

    Reservation payReservationFromCart(Customer customer, Item item) throws PaymentException, NegativeAmountTransactionException;
    Transaction payServiceFromCart(Customer customer, ServiceItem item) throws PaymentException, NegativeAmountTransactionException;
  
}
