package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
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
    public Reservation payReservationFromCart(Customer customer, Item item, ReservationType type) throws NegativeAmountTransactionException, PaymentException {
        if (item.getActivity().getPrice() < 0) {
            throw new NegativeAmountTransactionException(item.getActivity().getPrice());
        }
        String payment = bank.pay(customer, item.getActivity().getPrice()).orElseThrow(() -> new PaymentException(customer.getName(), item.getActivity().getPrice()));
        Transaction transaction = transactionCreator.createTransaction(customer, item.getActivity().getPrice(), payment);
        if (item.getType().equals(ReservationType.TIME_SLOT)) {


            return reservationCreator.createTimeSlotReservation(customer, (TimeSlotItem) item, transaction);
        }
        return reservationCreator.createGroupReservation(customer, (GroupItem) item, transaction);
        return reservationCreator.createReservation(customer, item, transaction, type);
    }
}
