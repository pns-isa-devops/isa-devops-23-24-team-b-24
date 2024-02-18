package teamb.w4e.cli.model;

public class CartElement {

    private CliActivity activity;

    public CartElement() {
    }

    public CartElement(CliActivity activity) {
        this.activity = activity;
    }

    public CliActivity getActivity() {
        return activity;
    }

    public void setActivity(CliActivity activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "CartElement{" +
                "activity=" + activity +
                "}";
    }
}
