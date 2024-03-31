package teamb.w4e.entities.reservations;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Card;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.transactions.Transaction;

@Entity
@DiscriminatorValue("GROUP")
public class GroupReservation extends Reservation {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
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
