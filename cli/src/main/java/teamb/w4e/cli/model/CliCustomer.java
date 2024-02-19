package teamb.w4e.cli.model;

// A cli side class being equivalent to the backend CustomerDTO, in terms of attributes
// so that the automatic JSON (de-)/serialization will make the two compatible on each side
public class CliCustomer {

    private Long id;
    private String name;
    private String creditCard;

    public CliCustomer(String name, String creditCard) {
        this.name = name;
        this.creditCard = creditCard;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreditCard() {
        return creditCard;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creditCard='" + creditCard + '\'' +
                '}';
    }
}
