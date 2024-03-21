package teamb.w4e.cli.model;

public class CliReservation {
    private Long id;
    private ReservationType type;
    private CliLeisure activity;
    private String date;
    private CliGroup group;
    private String skiPassType;
    private int skiPassDuration;

    public CliReservation() {
    }

    public CliReservation(ReservationType type, CliLeisure activity) {
        this.type = type;
        this.activity = activity;
    }

    public CliReservation(ReservationType type, CliLeisure activity, String date) {
        this.type = type;
        this.activity = activity;
        this.date = date;
    }

    public CliReservation(ReservationType type, CliLeisure activity, CliGroup group) {
        this.type = type;
        this.activity = activity;
        this.group = group;
    }

    public CliReservation(ReservationType type, CliLeisure activity, String skiPassType, int duration) {
        this.type = type;
        this.activity = activity;
        this.skiPassType = skiPassType;
        this.skiPassDuration = duration;
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

    public CliLeisure getActivity() {
        return activity;
    }

    public void setActivity(CliLeisure activity) {
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

    public String getSkiPassType() {
        return skiPassType;
    }

    public void setSkiPassType(String skiPassType) {
        this.skiPassType = skiPassType;
    }

    public int getSkiPassDuration() {
        return skiPassDuration;
    }

    public void setSkiPassDuration(int skiPassDuration) {
        this.skiPassDuration = skiPassDuration;
    }

    @Override
    public String toString() {
        String base = "id=" + id + '\'' +
                "type=" + type + '\'' +
                "activity=" + activity + '\'';
        return switch (type) {
            case TIME_SLOT -> "TimeSlotReservation{" + '\'' +
                    base + '\'' +
                    "date=" + date + '\'' +
                    "}";
            case GROUP -> "GroupReservation{" + '\'' +
                    base + '\'' +
                    "group=" + group + '\'' +
                    "}";
            case SKI_PASS -> "SkiPassReservation{" + '\'' +
                    base + '\'' +
                    "skiPassType=" + skiPassType + '\'' +
                    "duration=" + skiPassDuration + '\'' +
                    "}";
            default -> "Reservation{" + '\'' +
                    base + '\'' +
                    "}";
        };
            }
}
