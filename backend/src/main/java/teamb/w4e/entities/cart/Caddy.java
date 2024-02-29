package teamb.w4e.entities.cart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Embeddable
public class Caddy {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Item> trucs;

    public Set<Item> getActivities() {
        return trucs;
    }

    public void setActivities(Set<Item> activities) {
        this.trucs = activities;
    }

    public void clear() {
        this.trucs.clear();
    }

    @Override
    public String toString() {
        return "Caddy{" +
                ", activities=" + trucs +
                '}';
    }

    public Caddy() {
    }

    public Caddy(Set<Item> activities) {
        this.trucs = activities;
    }
}
