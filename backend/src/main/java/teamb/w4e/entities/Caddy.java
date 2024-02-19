package teamb.w4e.entities;

import jakarta.persistence.*;

import java.util.Set;

@Embeddable
public class Caddy {
    @Id
    private Long id;

    @ElementCollection
    private Set<Item> activities;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

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
                "id='" + id + '\'' +
                ", activities=" + activities +
                '}';
    }

    public Caddy() {
    }

    public Caddy(Long id, Set<Item> activities) {
        this.id = id;
        this.activities = activities;
    }
}
