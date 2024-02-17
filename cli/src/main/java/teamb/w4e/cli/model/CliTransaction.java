package teamb.w4e.cli.model;

public class CliTransaction {

    private Long id;
    private String customerName;
    private double amount;

    public CliTransaction() {
    }

    public CliTransaction(String customerName, double amount) {
        this.customerName = customerName;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CLITransaction{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
