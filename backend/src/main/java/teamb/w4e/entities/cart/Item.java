package teamb.w4e.entities.cart;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import teamb.w4e.entities.catalog.Leisure;
import teamb.w4e.entities.reservations.ReservationType;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationType type;

    @NotNull
    @ManyToOne
    private Leisure truc;

    protected Item() {
    }

    protected Item(ReservationType type, Leisure truc) {
        this.type = type;
        this.truc = truc;
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

    public Leisure getTruc() {
        return truc;
    }

    public void setTruc(Leisure truc) {
        this.truc = truc;
    }

    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                "truc=" + truc +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(truc, item.getTruc()) && Objects.equals(type, item.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, truc);
    }
}
