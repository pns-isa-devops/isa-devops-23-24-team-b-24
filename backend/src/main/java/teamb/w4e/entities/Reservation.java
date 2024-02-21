package teamb.w4e.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Activity activity;
    @NotBlank
    private String date;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private Card card;

    public Reservation() {
    }

    public Reservation(Activity activity, String date) {
        this.activity = activity;
        this.date = date;
    }

    public Reservation(Activity activity, String date, Card card) {
        this.activity = activity;
        this.date = date;
        this.card = card;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void setDate(String date) {
        this.date = date;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
