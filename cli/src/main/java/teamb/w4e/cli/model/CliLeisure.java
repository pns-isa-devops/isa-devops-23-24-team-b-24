package teamb.w4e.cli.model;

public class CliLeisure {
    private Long id;
    private String partnerName;
    private String name;
    private String description;
    private double price;
    private boolean booked; // activity (true) service (false)

    public CliLeisure() {
    }

    public CliLeisure(String partnerName, String name, String description, double price, boolean booked) {
        this.partnerName = partnerName;
        this.name = name;
        this.description = description;
        this.price = price;
        this.booked = booked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    @Override
    public String toString() {
        String type = booked ? "Activity" : "Service";
        return type + "{" +
                "id='" + id + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", isBooked=" + booked +
                '}';
    }
}
