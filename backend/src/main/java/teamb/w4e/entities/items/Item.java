package teamb.w4e.entities.items;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import teamb.w4e.entities.Partner;
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

    @NotBlank
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;

    @PositiveOrZero
    private double amount;

    protected Item() {
    }

    protected Item(ReservationType type, String name, Partner partner, double amount) {
        this.type = type;
        this.name = name;
        this.partner = partner;
        this.amount = amount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(type, item.getType()) &&Objects.equals(name, item.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}
