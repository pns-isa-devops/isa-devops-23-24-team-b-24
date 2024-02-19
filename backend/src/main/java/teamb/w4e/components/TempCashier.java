package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.interfaces.reservation.ReservationCreator;

@Service
public class TempCashier implements Payment {
    // This class is a placeholder for the payment system
    //TODO: Retrieve the payment system form Arnaud's branch

    private final ReservationCreator reservationCreator;

    @Autowired
    public TempCashier(ReservationCreator reservationCreator) {
        this.reservationCreator = reservationCreator;
    }


    @Override
    public Reservation payReservationFromCart(Customer customer, Item item) throws PaymentException {
        return reservationCreator.createReservation(customer,item);
    }
}
