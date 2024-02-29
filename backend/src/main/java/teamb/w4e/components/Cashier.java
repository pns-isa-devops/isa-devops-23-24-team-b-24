package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.cart.*;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.interfaces.TransactionCreator;
import teamb.w4e.interfaces.reservation.ReservationCreator;

@Service
public class Cashier implements Payment {
    private final Bank bank;
    private final TransactionCreator transactionCreator;

    private final ReservationCreator reservationCreator;

    @Autowired
    public Cashier(Bank bank, TransactionCreator transactionCreator, ReservationCreator reservationCreator) {
        this.bank = bank;
        this.transactionCreator = transactionCreator;
        this.reservationCreator = reservationCreator;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Reservation payReservationFromCart(Customer customer, Item item) throws NegativeAmountTransactionException, PaymentException {
        if (item.getTruc().getPrice() < 0) {
            throw new NegativeAmountTransactionException(item.getTruc().getPrice());
        }
        String payment = bank.pay(customer, item.getTruc().getPrice()).orElseThrow(() -> new PaymentException(customer.getName(), item.getTruc().getPrice()));
        Transaction transaction = transactionCreator.createTransaction(customer, item.getTruc().getPrice(), payment);
        ReservationType itemType = item.getType();
        return switch (itemType) {
            case TIME_SLOT -> reservationCreator.createTimeSlotReservation(customer, (TimeSlotItem) item, transaction);
            case GROUP -> reservationCreator.createGroupReservation(customer, (GroupItem) item, transaction);
            case SKI_PASS -> reservationCreator.createSkiPassReservation(customer, (SkiPassItem) item, transaction);
            case NONE -> null;
        };
    }
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction payServiceFromCart(Customer customer, ServiceItem item) throws NegativeAmountTransactionException, PaymentException {
        if (item.getTruc().getPrice() < 0) {
            throw new NegativeAmountTransactionException(item.getTruc().getPrice());
        }
        String payment = bank.pay(customer, item.getTruc().getPrice()).orElseThrow(() -> new PaymentException(customer.getName(), item.getTruc().getPrice()));
        return transactionCreator.createTransaction(customer, item.getTruc().getPrice(), payment);
    }

}
