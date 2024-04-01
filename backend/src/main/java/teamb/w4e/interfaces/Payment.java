package teamb.w4e.interfaces;

import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.entities.items.ServiceItem;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.exceptions.PaymentException;

public interface Payment {

    Reservation payReservationFromCart(Customer customer, Item item) throws PaymentException, NegativeAmountTransactionException;
    Transaction payServiceFromCart(Customer customer, ServiceItem item) throws PaymentException, NegativeAmountTransactionException;
    PointTransaction payAdvantageFromCart(Customer customer, Advantage advantage, Partner partner) throws NegativeAmountTransactionException;

}
