package teamb.w4e.cli.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CliPartner {

    private Long id;
    private String name;
    private Set<CliLeisure> leisure;

    public CliPartner(String name, Set<CliLeisure> leisure) {
        this.name = name;
        this.leisure = Objects.requireNonNullElseGet(leisure, HashSet::new);
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

    @Override
    public String toString() {
        return "CliPartner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leisure=" + leisure +
                '}';
    }
}
