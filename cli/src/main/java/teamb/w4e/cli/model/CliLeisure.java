package teamb.w4e.cli.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class CliLeisure {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean booked; // activity (true) service (false)
    private Set<CliAdvantage> advantages;

    public CliLeisure() {
    }

    public CliLeisure(String name, String description, double price, boolean booked, Set<CliAdvantage> advantages) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.booked = booked;
        this.advantages = Objects.requireNonNullElseGet(advantages, Set::of);
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
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public Set<CliAdvantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<CliAdvantage> advantages) {
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
                ", isBooked=" + booked +
                ", advantages=" + Arrays.toString(advantages.toArray()) +
                '}';
    }
}
