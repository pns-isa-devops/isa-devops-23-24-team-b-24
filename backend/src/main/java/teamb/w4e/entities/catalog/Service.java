package teamb.w4e.entities.catalog;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import teamb.w4e.entities.Partner;

import java.util.Set;

@Entity
@DiscriminatorValue("SERVICE")
public class Service extends Leisure {

    public Service() {
    }

    public Service(Partner partner, String name, String description, double price, Set<Advantage> advantages) {
        super(partner, name, description, price, false, advantages);
    }
}
