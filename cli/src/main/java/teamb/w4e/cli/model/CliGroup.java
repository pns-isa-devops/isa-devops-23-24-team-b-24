package teamb.w4e.cli.model;

import java.util.Set;

public class CliGroup {
    private Long id;
    private CliCustomer leader;
    private Set<CliCustomer> members;

    public CliGroup() {
    }

    public CliGroup(CliCustomer leader, Set<CliCustomer> members) {
        this.leader = leader;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CliCustomer getLeader() {
        return leader;
    }

    public void setLeader(CliCustomer leader) {
        this.leader = leader;
    }

    public Set<CliCustomer> getMembers() {
        return members;
    }

    public void setMembers(Set<CliCustomer> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", leader='" + leader + '\'' +
                ", members='" + members + '\'' +
                '}';
    }
}
