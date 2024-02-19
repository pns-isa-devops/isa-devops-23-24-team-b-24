package teamb.w4e.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Objects;

@Embeddable
public class Item {

    @NotNull
    @ManyToOne
    private Activity activity;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date reservationDate;

    public Item() {
    }

    public Item(Activity activity, Date reservationDate) {
        this.activity = activity;
        this.reservationDate = reservationDate;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "activity=" + activity +
                ", reservationDate=" + reservationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return activity.equals(item.activity) && reservationDate.equals(item.reservationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, reservationDate);
    }
}
