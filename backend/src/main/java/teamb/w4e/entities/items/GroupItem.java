package teamb.w4e.entities.items;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.reservations.ReservationType;

@Entity(name="group_items")
public class GroupItem extends Item {
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public GroupItem() {
    }

    public GroupItem(Activity activity, Group group) {
        super(ReservationType.GROUP, activity);
        this.group = group;
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
