package teamb.w4e.interfaces;

import teamb.w4e.entities.Activity;

public interface Scheduler {
    boolean checkAvailability(Activity activity, String date);

    boolean reserve(Activity activity, String date);
}
