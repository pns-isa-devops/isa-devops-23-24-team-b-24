package teamb.w4e.entities.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.Partner;

import java.util.Set;

@Entity(name = "leisure")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Leisure {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partner_id")
    private Partner partner;
    @NotBlank
    @Column(unique = true)
    private String name;
    @NotBlank
    private String description;
    @Positive
    private double price;

    private boolean booked;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Advantage> advantages;

    protected Leisure() {
    }

    protected Leisure(Partner partner, String name, String description, double price, boolean booked, Set<Advantage> advantages) {
        this.partner = partner;
        this.name = name;
        this.description = description;
        this.price = price;
        this.booked = booked;
        this.advantages = advantages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
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
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public Set<Advantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<Advantage> advantages) {
        this.advantages = advantages;
    }

    @Override
    public String toString() {
        String type = booked ? "Activity" : "Service";
        return type + "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", advantages=" + advantages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leisure leisure)) return false;
        return name.equals(leisure.name);
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode();
    }
}
