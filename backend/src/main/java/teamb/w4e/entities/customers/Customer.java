package teamb.w4e.entities.customers;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import teamb.w4e.entities.cart.Caddy;
import teamb.w4e.entities.reservations.Reservation;

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

    private Caddy caddy = new Caddy();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card = new Card();

    public Customer() {
    }

    public Customer(String n, String c) {
        this.name = n;
        this.creditCard = c;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void addReservation(Reservation r) {
        this.card.addReservation(r);
    }

    public Caddy getCaddy() {
        return caddy;
    }

    public void setCaddy(Caddy caddy) {
        this.caddy = caddy;
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
