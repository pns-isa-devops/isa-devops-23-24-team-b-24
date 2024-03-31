package teamb.w4e.cli.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CliPartner {
    private Long id;
    private String name;
    private Set<CliLeisure> leisure;
    private Set<CliAdvantage> advantages;

    public CliPartner(String name, Set<CliLeisure> leisure, Set<CliAdvantage> advantages) {
        this.name = name;
        this.leisure = Objects.requireNonNullElseGet(leisure, HashSet::new);
        this.advantages = Objects.requireNonNullElseGet(advantages, HashSet::new);
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

    public Set<CliLeisure> getLeisure() {
        return leisure;
    }

    public void setLeisure(Set<CliLeisure> leisure) {
        this.leisure = leisure;
    }

    public Set<CliAdvantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<CliAdvantage> advantages) {
        this.advantages = advantages;
    }

    @Override
    public String toString() {
        return "CliPartner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leisure=" + leisure +
                ", advantages=" + advantages +
                '}';
    }
}
