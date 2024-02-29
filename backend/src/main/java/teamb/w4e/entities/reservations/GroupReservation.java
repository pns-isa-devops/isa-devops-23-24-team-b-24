package teamb.w4e.entities.reservations;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import teamb.w4e.entities.*;

@Entity
@DiscriminatorValue("GROUP")
public class GroupReservation extends Reservation {
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public GroupReservation() {
    }

    public GroupReservation(Activity activity, Group group) {
        super(ReservationType.GROUP, activity);
        this.group = group;
    }

    public GroupReservation(Activity activity, Group group, Card card, Transaction transaction) {
        super(ReservationType.GROUP, activity, card, transaction);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
