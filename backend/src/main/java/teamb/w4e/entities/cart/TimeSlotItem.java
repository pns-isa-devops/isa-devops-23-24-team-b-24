package teamb.w4e.entities.cart;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.reservations.ReservationType;


@Entity(name = "time_slot_items")
public class TimeSlotItem extends Item {
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2]) (?:[01]\\d|2[0-3]):(?:[0-5]\\d)", message = "Invalid date")
    private String timeSlot;

    public TimeSlotItem() {
    }

    public TimeSlotItem(Activity activity, String timeSlot) {
        super(ReservationType.TIME_SLOT, activity);
        this.timeSlot = timeSlot;
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
