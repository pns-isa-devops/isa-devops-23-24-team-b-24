package teamb.w4e.cli.model;

public class CliAdvantage {
    private Long id;
    private String name;
    private AdvantageType type;
    private int points;

    public CliAdvantage() {
    }

    public CliAdvantage(String advantageName, AdvantageType advantageType, int points) {
        this.name = advantageName;
        this.type = advantageType;
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
        return "CliAdvantage{" +
                "id=" + id + '\'' +
                "advantageName='" + name + '\'' +
                ", advantageType=" + type +
                ", points=" + points +
                '}';
    }
}
