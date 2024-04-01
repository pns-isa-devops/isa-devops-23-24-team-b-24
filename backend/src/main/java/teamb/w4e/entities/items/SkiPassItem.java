package teamb.w4e.entities.items;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.reservations.ReservationType;


@Entity(name = "ski_pass_items")
public class SkiPassItem extends Item {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Activity activity;
    @Pattern(regexp = "^(day|hourly|half_day)$", message = "Invalid ski pass type")
    private String skiPassType;
    @Positive
    private int duration;

    public SkiPassItem() {
    }

    public SkiPassItem(Activity activity, String skiPassType, int duration) {
        super(ReservationType.SKI_PASS, activity.getName(), activity.getPartner(), activity.getPrice());
        this.activity = activity;
        this.skiPassType = skiPassType;
        this.duration = duration;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
