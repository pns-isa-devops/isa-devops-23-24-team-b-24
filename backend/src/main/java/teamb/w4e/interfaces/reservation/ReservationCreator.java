package teamb.w4e.interfaces.reservation;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.SkiPassItem;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.reservations.GroupReservation;
import teamb.w4e.entities.reservations.SkiPassReservation;
import teamb.w4e.entities.reservations.TimeSlotReservation;

public interface ReservationCreator {
    GroupReservation createGroupReservation(Customer customer, GroupItem item, Transaction transaction);
    SkiPassReservation createSkiPassReservation(Customer customer, SkiPassItem item, Transaction transaction);

    TimeSlotReservation createTimeSlotReservation(Customer customer, TimeSlotItem item, Transaction transaction);


}
