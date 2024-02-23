package teamb.w4e.cli.model;

// A cli side class being equivalent to the backend CustomerDTO, in terms of attributes
// so that the automatic JSON (de-)/serialization will make the two compatible on each side
public class CliCustomer {

    private Long id;
    private String name;
    private String creditCard;
    private CliCard card;

    public CliCustomer(String name, String creditCard) {
        this.name = name;
        this.creditCard = creditCard;
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

    public CliCard getCard() {
        return card;
    }

    public void setCard(CliCard card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creditCard='" + creditCard + '\'' +
                ", card='" + card + '\'' +
                '}';
    }
}
