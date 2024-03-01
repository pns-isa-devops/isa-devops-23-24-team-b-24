package teamb.w4e.cli.model.cart;

import teamb.w4e.cli.model.CliGroup;
import teamb.w4e.cli.model.CliLeisure;
import teamb.w4e.cli.model.ReservationType;

public class CartElement {
    private Long id;
    private ReservationType type;
    private CliLeisure leisure;
    private String date;
    private CliGroup group;
    private String skiPassType;
    private int duration;

    protected CartElement() {
    }

    public CartElement(ReservationType type, CliLeisure leisure) {
        this.type = type;
        this.leisure = leisure;
    }

    public CartElement(ReservationType type, CliLeisure leisure, String date) {
        this.type = type;
        this.leisure = leisure;
        this.date = date;
    }

    public CartElement(ReservationType type, CliLeisure leisure, CliGroup group) {
        this.type = type;
        this.leisure = leisure;
        this.group = group;
    }

    public CartElement(ReservationType type, CliLeisure leisure, String skiPassType, int duration) {
        this.type = type;
        this.leisure = leisure;
        this.skiPassType = skiPassType;
        this.duration = duration;
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

    public CliLeisure getLeisure() {
        return leisure;
    }

    public void setLeisure(CliLeisure leisure) {
        this.leisure = leisure;
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

    public String getSkiPassType() {
        return skiPassType;
    }

    public void setSkiPassType(String skiPassType) {
        this.skiPassType = skiPassType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        String base = "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", leisure='" + leisure + '\'';
        if (type.equals(ReservationType.SKI_PASS)) {
            return "SkiElement{" +
                    base +
                    ", skiPassType='" + skiPassType + '\'' +
                    ", duration=" + duration +
                    '}';
        } else if (type.equals(ReservationType.GROUP)) {
            return "GroupElement{" +
                    base +
                    ", date='" + date + '\'' +
                    ", group='" + group + '\'' +
                    '}';
        } else if (type.equals(ReservationType.TIME_SLOT)) {
            return "TimeSlotReservationDTO{" +
                    base +
                    ", date='" + date + '\'' +
                    '}';
        } else {
            return "ServiceElement{" +
                    base +
                    '}';
        }
    }
}
