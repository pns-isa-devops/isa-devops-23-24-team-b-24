package teamb.w4e.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Customer customer;

    @NotBlank
    @Pattern(regexp = "\\d{10}+", message = "Invalid amount format")
    private double amount;

    public Transaction() {
    }

    public Transaction(Customer customer, double amount) {
        this.customer = customer;
        this.amount = amount;
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
