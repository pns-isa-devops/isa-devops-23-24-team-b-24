package teamb.w4e.interfaces.reservation;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;

public interface ReservationCreator {
    Reservation createReservation(Customer customer, Item item);


}
