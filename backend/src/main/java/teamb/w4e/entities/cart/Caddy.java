package teamb.w4e.entities.cart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Embeddable
public class Caddy {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Item> leisure;

    public Set<Item> getLeisure() {
        return leisure;
    }

    public void setLeisure(Set<Item> leisure) {
        this.leisure = leisure;
    }

    public void clear() {
        this.leisure.clear();
    }

    @Override
    public String toString() {
        return "Caddy{" +
                ", leisure=" + leisure +
                '}';
    }

    public Caddy() {
    }

    public Caddy(Set<Item> activities) {
        this.leisure = activities;
    }
}
