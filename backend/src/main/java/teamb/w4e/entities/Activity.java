package teamb.w4e.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Advantage> advantages;

    public Activity() {
    }

    public Activity(String name, String description, Set<Advantage> advantages) {
        this.name = name;
        this.description = description;
        this.advantages = advantages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Advantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<Advantage> advantages) {
        this.advantages = advantages;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", advantages='" + advantages + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
