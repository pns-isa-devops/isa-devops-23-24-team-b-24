package teamb.w4e.entities.reservations;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Card;
import teamb.w4e.entities.transactions.Transaction;

@Entity
@DiscriminatorValue("SKI_PASS")
public class SkiPassReservation extends Reservation {
    @Pattern(regexp = "^(day|hourly|half_day)", message = "Invalid ski pass type")
    private String skiPassType;
    @Positive
    private int duration;

    public SkiPassReservation() {
    }

    public SkiPassReservation(Activity activity, String skiPassType, int duration) {
        super(ReservationType.SKI_PASS, activity);
        this.skiPassType = skiPassType;
        this.duration = duration;
    }

    public SkiPassReservation(Activity activity, String skiPassType, int duration, Card card, Transaction transaction) {
        super(ReservationType.SKI_PASS, activity, card, transaction);
        this.skiPassType = skiPassType;
        this.duration = duration;
    }

    public String getSkiPassType() {
        return skiPassType;
    }

    public void setSkiPassType(String skiPassType) {
        this.skiPassType = skiPassType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
