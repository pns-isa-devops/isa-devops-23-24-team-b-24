package teamb.w4e.cli.model;

public class CliPointTransaction {
    private Long id;
    private CliCustomer customer;
    private int points;
    private String trade;
    public CliPointTransaction() {
    }

    public CliPointTransaction(CliCustomer customer, int points) {
        this.customer = customer;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CliCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(CliCustomer customer) {
        this.customer = customer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    @Override
    public String toString() {
        return "CliPointTransaction{" +
                "id=" + id +
                ", customerName='" + customer.getName() + '\'' +
                ", points=" + points + '\'' +
                ", trade=" + trade + '\'' +
                '}';
    }
}
