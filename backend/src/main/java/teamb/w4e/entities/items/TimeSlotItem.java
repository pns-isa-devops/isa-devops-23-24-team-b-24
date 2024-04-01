package teamb.w4e.entities.items;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.reservations.ReservationType;


@Entity(name = "time_slot_items")
public class TimeSlotItem extends Item {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Activity activity;
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2]) (?:[01]\\d|2[0-3]):(?:[0-5]\\d)", message = "Invalid date")
    private String timeSlot;

    public TimeSlotItem() {
    }

    public TimeSlotItem(Activity activity, String timeSlot) {
        super(ReservationType.TIME_SLOT, activity.getName(), activity.getPartner(), activity.getPrice());
        this.activity = activity;
        this.timeSlot = timeSlot;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlotItem that)) return false;
        if (!super.equals(o)) return false;
        return timeSlot.equals(that.timeSlot);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + timeSlot.hashCode();
    }
}
