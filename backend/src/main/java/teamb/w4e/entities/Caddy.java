package teamb.w4e.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Embeddable
public class Caddy {
    @ElementCollection
    @Fetch(FetchMode.JOIN)
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
