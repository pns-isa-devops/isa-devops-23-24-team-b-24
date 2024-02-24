package teamb.w4e.interfaces.reservation;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.reservations.GroupReservation;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.reservations.TimeSlotReservation;
import teamb.w4e.entities.reservations.ReservationType;

public interface ReservationCreator {
    GroupReservation createGroupReservation(Customer customer, GroupItem item, Transaction transaction);
    Reservation createReservation(Customer customer, Item item, Transaction transaction, ReservationType type);

    TimeSlotReservation createTimeSlotReservation(Customer customer, TimeSlotItem item, Transaction transaction);


}
