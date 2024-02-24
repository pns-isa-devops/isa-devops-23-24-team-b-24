package teamb.w4e.entities.cart;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Item {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationType type;

    @NotNull
    @ManyToOne
    private Activity activity;

    protected Item() {
    }

    protected Item(ReservationType type, Activity activity) {
        this.type = type;
        this.activity = activity;
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

    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                "activity=" + activity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(activity, item.getActivity()) && Objects.equals(type, item.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, activity);
    }
}
