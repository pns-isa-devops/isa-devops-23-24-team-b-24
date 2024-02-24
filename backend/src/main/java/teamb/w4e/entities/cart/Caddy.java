package teamb.w4e.entities.cart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Embeddable
public class Caddy {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Item> activities;

    public Set<Item> getActivities() {
        return activities;
    }

    public void setActivities(Set<Item> activities) {
        this.activities = activities;
    }

    public void clear() {
        this.activities.clear();
    }

    @Override
    public String toString() {
        return "Caddy{" +
                ", activities=" + activities +
                '}';
    }

    public Caddy() {
    }

    public Caddy(Set<Item> activities) {
        this.activities = activities;
    }
}
