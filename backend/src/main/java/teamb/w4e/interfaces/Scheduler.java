package teamb.w4e.interfaces;

import teamb.w4e.entities.Activity;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.TimeSlotReservation;

public interface Scheduler {
    boolean checkAvailability(Activity activity, String date);

    boolean reserve(Activity activity, String date);
}
