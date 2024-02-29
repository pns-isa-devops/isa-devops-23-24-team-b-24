package teamb.w4e.entities.reservations;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Card;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.Truc;

@Entity(name = "reservations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "reservation_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private ReservationType type;
    @ManyToOne
    private Activity activity;
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private Card card;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private Transaction transaction;

    protected Reservation() {
    }

    protected Reservation(ReservationType type, Activity activity) {
        this.type = type;
        this.activity = activity;
    }

    protected Reservation(ReservationType type, Activity activity, Card card, Transaction transaction) {
        this.type = type;
        this.activity = activity;
        this.card = card;
        this.transaction = transaction;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ReservationType getType() {
        return type;
    }

    public void setType(ReservationType type) {
        this.type = type;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
