package teamb.w4e.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Set;

@Entity
@DiscriminatorValue("ACTIVITY")
public class Activity extends Truc {

    public Activity() {
    }

    public Activity(String name, String description, double price, Set<Advantage> advantages) {
        super(name, description, price, true, advantages);
    }
}
