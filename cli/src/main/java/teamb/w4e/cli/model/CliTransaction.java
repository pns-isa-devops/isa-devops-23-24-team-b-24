package teamb.w4e.cli.model;

public class CliTransaction {
    private Long id;
    private CliCustomer customer;
    private double amount;
    private String paymentId;

    public CliTransaction() {
    }

    public CliTransaction(CliCustomer customerName, double amount) {
        this.customer = customerName;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "CliTransaction{" +
                "id=" + id +
                ", customerName='" + customer.getName() + '\'' +
                ", amount=" + amount + '\'' +
                ", paymentId=" + paymentId + '\'' +
                '}';
    }
}
