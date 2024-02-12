package teamb.w4e.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customer_groups")
public class Group {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer leader;

    @ManyToMany
    private List<Customer> members;


    public Group() {
    }

    public Group(Customer leader, List<Customer> members) {
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

    public void setMembers(List<Customer> members) {
        this.members = members;
    }

    public List<Customer> getMembers() {
        return members;
    }
}
