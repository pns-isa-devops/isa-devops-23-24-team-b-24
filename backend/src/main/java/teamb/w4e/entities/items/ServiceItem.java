package teamb.w4e.entities.items;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.entities.reservations.ReservationType;

@Entity(name = "service_items")
public class ServiceItem extends Item {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Service service;

    public ServiceItem() {
    }

    public ServiceItem(Service service) {
        super(ReservationType.SERVICE, service.getName(), service.getPartner(), service.getPrice());
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceItem that)) return false;
        if (!super.equals(o)) return false;
        return service.equals(that.service);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + service.hashCode();
    }
}
