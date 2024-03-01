package teamb.w4e.entities.cart;

import jakarta.persistence.Entity;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.entities.reservations.ReservationType;

@Entity(name ="service_items")
public class ServiceItem extends Item {

    public ServiceItem() {
    }

    public ServiceItem(Service service) {
        super(ReservationType.NONE, service);
    }
}
