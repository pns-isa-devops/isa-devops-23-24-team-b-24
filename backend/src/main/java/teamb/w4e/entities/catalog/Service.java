package teamb.w4e.entities.catalog;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Set;

@Entity
@DiscriminatorValue("SERVICE")
public class Service extends Leisure {

    public Service() {
    }

    public Service(String name, String description, double price, Set<Advantage> advantages) {
        super(name, description, price, false, advantages);
    }
}
