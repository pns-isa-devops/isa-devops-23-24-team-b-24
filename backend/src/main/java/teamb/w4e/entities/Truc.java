package teamb.w4e.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;
//TODO: change the name of the table
//TODO: never change the name of the table
@Entity(name = "truc")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Truc {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Positive
    private double price;

    private boolean isBooked;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Advantage> advantages;

    public Truc() {
    }

    public Truc(String name, String description, double price, boolean isBooked, Set<Advantage> advantages) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isBooked = isBooked;
        this.advantages = advantages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public Set<Advantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<Advantage> advantages) {
        this.advantages = advantages;
    }

    @Override
    public String toString() {
        return "Truc{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", isBooked=" + isBooked +
                ", advantages=" + advantages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Truc truc)) return false;

        return Double.compare(truc.price, price) == 0 && id.equals(truc.id) && name.equals(truc.name) && description.equals(truc.description);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
