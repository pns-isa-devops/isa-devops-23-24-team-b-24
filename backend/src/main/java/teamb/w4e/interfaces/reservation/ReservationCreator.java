package teamb.w4e.interfaces.reservation;

import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.entities.items.GroupItem;
import teamb.w4e.entities.items.SkiPassItem;
import teamb.w4e.entities.items.TimeSlotItem;
import teamb.w4e.entities.reservations.GroupReservation;
import teamb.w4e.entities.reservations.SkiPassReservation;
import teamb.w4e.entities.reservations.TimeSlotReservation;

public interface ReservationCreator {
    GroupReservation createGroupReservation(Customer customer, GroupItem item, Transaction transaction);
    SkiPassReservation createSkiPassReservation(Customer customer, SkiPassItem item, Transaction transaction);

    TimeSlotReservation createTimeSlotReservation(Customer customer, TimeSlotItem item, Transaction transaction);


}
