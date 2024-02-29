package teamb.w4e.entities.cart;

import jakarta.persistence.Entity;
import teamb.w4e.entities.Service;
import teamb.w4e.entities.Truc;
import teamb.w4e.entities.reservations.ReservationType;

@Entity(name ="service-items")
public class ServiceItem extends Item {

    public ServiceItem() {
    }

    public ServiceItem(Service service) {
        super(ReservationType.NONE, service);
    }
}

// j'ai fix je pense tout les renommage dasn Cashier CartHandler et Booker
// mais il reste un bug dans CartHandler Cheffe
// thumb up
// j'essayee de fix en meme temps j'ai l'impression d'etre un raton laveur
