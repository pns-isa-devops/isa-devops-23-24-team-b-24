package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.*;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.*;
import teamb.w4e.interfaces.reservation.ReservationCreator;

@Service
public class Cashier implements Payment, AdvantagePayment {
    private final Bank bank;
    private final TransactionCreator transactionCreator;
    private final PointTransactionCreator pointTransactionCreator;
    private final ReservationCreator reservationCreator;
    private final PointAdder pointAdder;

    @Autowired
    public Cashier(Bank bank, TransactionCreator transactionCreator, PointTransactionCreator pointTransactionCreator, ReservationCreator reservationCreator, PointAdder pointAdder) {
        this.bank = bank;
        this.transactionCreator = transactionCreator;
        this.pointTransactionCreator = pointTransactionCreator;
        this.reservationCreator = reservationCreator;
        this.pointAdder = pointAdder;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Reservation payReservationFromCart(Customer customer, Item item) throws NegativeAmountTransactionException, PaymentException {
        double price = item.getAmount();
        if (price < 0) {
            throw new NegativeAmountTransactionException(item.getAmount());
        }
        String payment = bank.pay(customer, item.getAmount()).orElseThrow(() -> new PaymentException(customer.getName(), price));
        customer.getCard().addPoints(pointAdder.convertPriceToPoints(price));
        pointTransactionCreator.createPointTransactionWithPartner(customer, pointAdder.convertPriceToPoints(price), item.getPartner());
        Transaction transaction = transactionCreator.createTransaction(customer, price, payment);
        ReservationType itemType = item.getType();
        if (itemType.equals(ReservationType.TIME_SLOT)) {
            return reservationCreator.createTimeSlotReservation(customer, (TimeSlotItem) item, transaction);
        } else if (itemType.equals(ReservationType.GROUP)) {
            return reservationCreator.createGroupReservation(customer, (GroupItem) item, transaction);
        } else {
            return reservationCreator.createSkiPassReservation(customer, (SkiPassItem) item, transaction);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction payServiceFromCart(Customer customer, ServiceItem item) throws NegativeAmountTransactionException, PaymentException {
        if (item.getAmount() < 0) {
            throw new NegativeAmountTransactionException(item.getAmount());
        }
        String payment = bank.pay(customer, item.getAmount()).orElseThrow(() -> new PaymentException(customer.getName(), item.getAmount()));
        customer.getCard().addPoints(pointAdder.convertPriceToPoints(item.getAmount()));
        pointTransactionCreator.createPointTransactionWithPartner(customer, pointAdder.convertPriceToPoints(item.getAmount()), item.getPartner());
        return transactionCreator.createTransaction(customer, item.getAmount(), payment);
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public PointTransaction payAdvantageFromCart(Customer customer, Advantage advantage, Partner partner) throws NegativeAmountTransactionException {
        if (customer.getCard().getPoints() < advantage.getPoints()) {
            throw new NegativeAmountTransactionException(advantage.getPoints());
        }
        int points = advantage.getPoints();
        customer.getCard().removePoints(points);
        return pointTransactionCreator.createPointTransactionWithPartner(customer, points, partner);
    }
}
