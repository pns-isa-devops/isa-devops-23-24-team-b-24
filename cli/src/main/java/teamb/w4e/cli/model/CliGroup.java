package teamb.w4e.cli.model;

import java.util.List;

public class CliGroup {

    private Long id;
    private String leaderName;
    private List<String> membersNames;

    public CliGroup() {
    }

    public CliGroup(String leaderName, List<String> membersNames) {
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

    public List<String> getMembersNames() {
        return membersNames;
    }

    public void setMembersNames(List<String> membersNames) {
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
