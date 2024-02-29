package teamb.w4e.connectors;

import org.springframework.stereotype.Component;
import teamb.w4e.entities.Activity;
import teamb.w4e.interfaces.Scheduler;

@Component
public class SchedulerProxy implements Scheduler {
    @Override
    public boolean checkAvailability(Activity activity, String date) {
        return true;
    }

    @Override
    public boolean reserve(Activity activity, String date) {
        return true;
    }
}
