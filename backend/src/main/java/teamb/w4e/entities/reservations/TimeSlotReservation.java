package teamb.w4e.entities.reservations;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Card;
import teamb.w4e.entities.transactions.Transaction;
@Entity
@DiscriminatorValue("TIME_SLOT")
public class TimeSlotReservation extends Reservation {
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2]) (?:[01]\\d|2[0-3]):(?:[0-5]\\d)", message = "Invalid date")
    private String timeSlot;

    public TimeSlotReservation() {
    }

    public TimeSlotReservation(Activity activity, String timeSlot) {
        super(ReservationType.TIME_SLOT, activity);
        this.timeSlot = timeSlot;
    }

    public TimeSlotReservation(Activity activity, String timeSlot, Card card, Transaction transaction) {
        super(ReservationType.TIME_SLOT, activity, card, transaction);
        this.timeSlot = timeSlot;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
