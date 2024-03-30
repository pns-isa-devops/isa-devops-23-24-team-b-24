package teamb.w4e.entities.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import teamb.w4e.entities.Partner;

@Entity
public class Advantage {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @Enumerated()
    private AdvantageType type;

    @Positive
    private int points;

    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    private Partner partner;

    public Advantage() {
    }

    public Advantage(Partner partner, String name, AdvantageType type, int points) {
        this.partner = partner;
        this.name = name;
        this.type = type;
        this.points = points;
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

    public AdvantageType getType() {
        return type;
    }

    public void setType(AdvantageType type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    @Override
    public String toString() {
        return "Advantage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", points='" + points + '\'' +
                '}';
    }
}
