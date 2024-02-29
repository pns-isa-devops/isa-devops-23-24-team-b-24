package teamb.w4e.cli.model.cart;

import teamb.w4e.cli.model.CliActivity;
import teamb.w4e.cli.model.CliGroup;
import teamb.w4e.cli.model.ReservationType;

public class CartElement {
    private Long id;
    private ReservationType type;
    private CliActivity activity;
    private String date;

    private CliGroup group;

    protected CartElement() {
    }

    protected CartElement(ReservationType type, CliActivity activity) {
        this.type = type;
        this.activity = activity;
    }

    public CartElement(ReservationType type, CliActivity activity, String date) {
        this.type = type;
        this.activity = activity;
        this.date = date;
    }

    public CartElement(ReservationType type, CliActivity activity, CliGroup group) {
        this.type = type;
        this.activity = activity;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReservationType getType() {
        return type;
    }

    public void setType(ReservationType type) {
        this.type = type;
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

    public CliGroup getGroup() {
        return group;
    }

    public void setGroup(CliGroup group) {
        this.group = group;
    }


    @Override
    public String toString() {
        String base = "id=" + id + '\'' +
                "type=" + type + '\'' +
                "activity=" + activity + '\'';
        if (date != null) {
            return "TimeSlotElement{" +
                    base + '\'' +
                    "date='" + date + '\'' +
                    '}';
        } else if (group != null) {
            return "GroupElement{" +
                    base + '\'' +
                    "group=" + group + '\'' +
                    '}';
        }
        return "CartElement{" +
                base + '\'' +
                '}';
    }
}
