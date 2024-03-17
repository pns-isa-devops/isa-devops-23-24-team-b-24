package teamb.w4e.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import teamb.w4e.entities.catalog.Leisure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Partner {

    @Id
    @GeneratedValue
    private Long id; // Whether Long/Int or UUID are better primary keys, exposable outside is a vast issue, keep it simple here

    @NotBlank
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "partner")
    private Set<Leisure> leisure = new HashSet<>();
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
