package teamb.w4e.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private Customer leader;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "members",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id")
    )
    private Set<Customer> members;


    public Group() {
    }

    public Group(Customer leader, Set<Customer> members) {
        this.leader = leader;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getLeader() {
        return leader;
    }

    public void setLeader(Customer leader) {
        this.leader = leader;
    }

    public Set<Customer> getMembers() {
        return members;
    }

    public void setMembers(Set<Customer> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group group)) return false;
        return Objects.equals(leader, group.leader) && Objects.equals(members, group.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leader, members);
    }
}
