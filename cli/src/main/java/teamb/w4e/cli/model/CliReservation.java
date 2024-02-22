package teamb.w4e.cli.model;

public class CliReservation {

    private Long id;
    private CliActivity activity;

    private String date;

    private String status;

    public CliReservation() {
    }

    public CliReservation(CliActivity activity, String date, String status) {
        this.activity = activity;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CliActivity getActivity() {
        return activity;
    }

    public void setActivity(CliActivity activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", activity='" + activity + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
