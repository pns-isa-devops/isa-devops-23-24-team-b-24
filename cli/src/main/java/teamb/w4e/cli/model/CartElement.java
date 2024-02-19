package teamb.w4e.cli.model;

public class CartElement {

    private CliActivity activity;

    private String date;

    public CartElement() {
    }

    public CartElement(CliActivity activity, String date) {
        this.activity = activity;
        this.date = date;
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

    @Override
    public String toString() {
        return "CartElement{" + '\'' +
                "activity=" + activity + '\'' +
                ", date=" + date + '\'' +
                "}";
    }
}
