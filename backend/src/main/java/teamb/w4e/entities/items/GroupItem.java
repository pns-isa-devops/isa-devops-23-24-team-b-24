package teamb.w4e.entities.items;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.reservations.ReservationType;

@Entity(name="group_items")
public class GroupItem extends Item {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Activity activity;
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public GroupItem() {
    }

    public GroupItem(Activity activity, Group group) {
        super(ReservationType.GROUP, activity.getName(), activity.getPartner(), activity.getPrice());
        this.activity = activity;
        this.group = group;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupItem that)) return false;
        if (!super.equals(o)) return false;
        return group.equals(that.group);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + group.hashCode();
    }
}
