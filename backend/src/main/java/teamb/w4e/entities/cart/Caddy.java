package teamb.w4e.entities.cart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.items.Item;

import java.util.HashSet;
import java.util.Set;

@Embeddable
public class Caddy {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<Item> catalogItem = new HashSet<>();

    public Set<Item> getCatalogItem() {
        return catalogItem;
    }

    public void setCatalogItem(Set<Item> catalogItem) {
        this.catalogItem = catalogItem;
    }

    public void clear() {
        this.catalogItem.clear();
    }

    @Override
    public String toString() {
        return "Caddy{" +
                ", leisure=" + catalogItem +
                '}';
    }

    public Caddy() {
    }

    public Caddy(Set<Item> catalogItem) {
        this.catalogItem = catalogItem;
    }
}
