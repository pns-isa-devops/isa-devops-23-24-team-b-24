package teamb.w4e.entities.cart;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.reservations.ReservationType;

@Entity
@DiscriminatorValue("SKI_PASS")
public class SkiPassItem extends Item {
    private String skiPassType;
    private int duration;

    public SkiPassItem() {
    }

    public SkiPassItem(Activity activity, String skiPassType, int duration) {
        super(ReservationType.SKI_PASS, activity);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkiPassItem that)) return false;
        if (!super.equals(o)) return false;
        return duration == that.duration && skiPassType.equals(that.skiPassType);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + skiPassType.hashCode() + duration;
    }
}
