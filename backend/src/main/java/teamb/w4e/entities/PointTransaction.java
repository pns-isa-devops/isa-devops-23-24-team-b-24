package teamb.w4e.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "transactions")
public class PointTransaction {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // @ManyToOne
    // @JoinColumn(name = "issuer_id")
    // private Partner issuer;

    @Positive
    private int amount;

    @NotBlank
    private String paymentId;

    public PointTransaction() {
    }

    public PointTransaction(Customer customer, int amount) {//, Partner issuer) {
        this.customer = customer;
        this.amount = amount;
        // this.issuer = issuer;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
