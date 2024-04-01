package teamb.w4e.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.Leisure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Partner {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Leisure> leisure = new HashSet<>();

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Advantage> advantages = new HashSet<>();

    public Partner() {
    }

    public Partner(String n) {
        this.name = n;

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

    public Set<Leisure> getLeisure() {
        return leisure;
    }

    public void setLeisure(Set<Leisure> leisure) {
        this.leisure = leisure;
    }

    public Set<Advantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<Advantage> advantages) {
        this.advantages = advantages;
    }

    public void addLeisure(Leisure l) {
        this.leisure.add(l);
        l.setPartner(this);
    }

    public void removeLeisure(Leisure l) {
        this.leisure.remove(l);
        l.setPartner(null);
    }

    public void addAdvantage(Advantage a) {
        this.advantages.add(a);
        a.setPartner(this);
    }

    public void removeAdvantage(Advantage a) {
        this.advantages.remove(a);
        a.setPartner(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partner partner)) return false;
        return Objects.equals(name, partner.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
