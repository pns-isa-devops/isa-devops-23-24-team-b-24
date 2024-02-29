package teamb.w4e.cli.model;

import java.util.Arrays;
import java.util.Set;

public class CliActivity {
    private Long id;
    private String name;
    private String description;

    private double price;

    private Set<CliAdvantage> advantages;

    public CliActivity() {
    }

    public CliActivity(String name, String description, double price, Set<CliAdvantage> advantages) {
        this.name = name;
        this.description = description;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<CliAdvantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<CliAdvantage> advantages) {
        this.advantages = advantages;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", advantages='" + Arrays.toString(advantages.toArray()) + '\'' +
                '}';
    }
}
