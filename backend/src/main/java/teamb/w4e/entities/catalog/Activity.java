package teamb.w4e.entities.catalog;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import teamb.w4e.entities.Partner;

import java.util.Set;

@Entity
@DiscriminatorValue("ACTIVITY")
public class Activity extends Leisure {
    public Activity() {
    }

    public Activity(Partner partner, String name, String description, double price, Set<Advantage> advantages) {
        super(partner, name, description, price, true, advantages);
    }
}
