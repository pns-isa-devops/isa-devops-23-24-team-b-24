package teamb.w4e.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "customer_groups")
public class Group {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Customer leader;

    @ManyToMany
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

    public Customer getLeader() {
        return leader;
    }

    public Set<Customer> getMembers() {
        return members;
    }
}
