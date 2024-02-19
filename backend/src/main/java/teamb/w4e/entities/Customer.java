package teamb.w4e.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id; // Whether Long/Int or UUID are better primary keys, exposable outside is a vast issue, keep it simple here

    @NotBlank
    @Column(unique = true)
    private String name;

    @Pattern(regexp = "\\d{10}+", message = "Invalid creditCardNumber")
    private String creditCard;

//    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "customer")
//    private Set<Order> orders = new HashSet<>();
    private Caddy caddy = new Caddy();

    public Customer() {
    }

    public Customer(String n, String c) {
        this.name = n;
        this.creditCard = c;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

//    public void addOrder(Order o) {
//        this.orders.add(o);
//    }
//
//    public Set<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(Set<Order> orders) {
//            this.orders = orders;
//    }

    public Caddy getCaddy() {
        return caddy;
    }

    public void setCaddy(Caddy caddy) {
        this.caddy = caddy;
    }

    public void clearCaddy() {
        this.caddy.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(name, customer.name) && Objects.equals(creditCard, customer.creditCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, creditCard);
    }

}
