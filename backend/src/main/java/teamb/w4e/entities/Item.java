package teamb.w4e.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Objects;

@Embeddable
public class Item {

    @NotNull
    @ManyToOne
    private Activity activity;

    @NotBlank
    private String date;

    public Item() {
    }

    public Item(Activity activity, String reservationDate) {
        this.activity = activity;
        this.date = reservationDate;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String reservationDate) {
        this.date = reservationDate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "activity=" + activity +
                ", reservationDate=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return activity.equals(item.activity) && date.equals(item.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, date);
    }
}
