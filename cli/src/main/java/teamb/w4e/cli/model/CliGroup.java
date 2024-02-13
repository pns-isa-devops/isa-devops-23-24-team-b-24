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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public Set<String> getMembersNames() {
        return membersNames;
    }

    public void setMembersNames(Set<String> membersNames) {
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
