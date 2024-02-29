package teamb.w4e.entities.cart;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.reservations.ReservationType;

@Entity
@DiscriminatorValue("GROUP")
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
}
