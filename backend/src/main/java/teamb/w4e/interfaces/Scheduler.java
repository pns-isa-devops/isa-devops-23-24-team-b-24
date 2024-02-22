package teamb.w4e.interfaces;

import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Reservation;

import java.util.Date;

public interface Scheduler {
    boolean checkAvailability(Activity activity, String date);

    Reservation reserve(Activity activity, String date);
}
