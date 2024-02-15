package teamb.w4e.cli.model;

import java.util.Set;

public class CliGroup {
    private Long id;
    private String leaderName;
    private Set<String> membersNames;

    public CliGroup() {
    }

    public CliGroup(String leaderName, Set<String> membersNames) {
        this.leaderName = leaderName;
        this.membersNames = membersNames;
    }

    @Override
    public String toString() {
        return "CliGroup{" +
                "id='" + id + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", membersNames=" + membersNames +
                '}';
    }
}
