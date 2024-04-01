package teamb.w4e.entities.items;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.reservations.ReservationType;

@Entity(name = "advantage_items")
public class AdvantageItem extends Item {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Advantage advantage;

    public AdvantageItem() {
    }

    public AdvantageItem(Advantage advantage) {
        super(ReservationType.NONE, advantage.getName(), advantage.getPartner(), advantage.getPoints());
        this.advantage = advantage;
    }

    public Advantage getAdvantage() {
        return advantage;
    }

    public void setAdvantage(Advantage advantage) {
        this.advantage = advantage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvantageItem that)) return false;
        if (!super.equals(o)) return false;
        return advantage.equals(that.advantage);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + advantage.hashCode();
    }

}
