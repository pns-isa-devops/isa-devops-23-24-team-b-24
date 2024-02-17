package teamb.w4e.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Advantage {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @Enumerated()
    private AdvantageType type;

    @Positive
    private int points;

    public Advantage() {
    }

    public Advantage(String name, AdvantageType type, int points) {
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
