package teamb.w4e.entities;

import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    private Transaction transaction;

    public Reservation() {
    }

    public Reservation(Activity activity, String date) {
        this.activity = activity;
        this.date = date;
    }

    public Reservation(Activity activity, String date, Card card, Transaction transaction) {
        this.activity = activity;
        this.date = date;
        this.card = card;
        this.transaction = transaction;
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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}
