package teamb.w4e.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "customer_groups")
public class Group {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer leader;

    @ManyToMany
    private Set<Customer> members;


    public Group() {
    }

    public Group(Customer leader, Set<Customer> members) {
        this.leader = leader;
        this.members = members;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setLeader(Customer leader) {
        this.leader = leader;
    }

    public Customer getLeader() {
        return leader;
    }

    public void setMembers(Set<Customer> members) {
        this.members = members;
    }

    public Set<Customer> getMembers() {
        return members;
    }
}
