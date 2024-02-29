package teamb.w4e.cli.model;

public class CliReservation {

    private Long id;

    private ReservationType type;
    private CliActivity activity;
    private String date;
    private CliGroup group;
    private String skiPassType;
    private int duration;

    public CliReservation() {
    }

    public CliReservation(ReservationType type, CliActivity activity) {
        this.type = type;
        this.activity = activity;
    }

    public CliReservation(ReservationType type, CliActivity activity, String date) {
        this.type = type;
        this.activity = activity;
        this.date = date;
    }

    public CliReservation(ReservationType type, CliActivity activity, CliGroup group) {
        this.type = type;
        this.activity = activity;
        this.group = group;
    }

    public CliReservation(ReservationType type, CliActivity activity, String skiPassType, int duration) {
        this.type = type;
        this.activity = activity;
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
        String base = "id=" + id + '\'' +
                "type=" + type + '\'' +
                "activity=" + activity + '\'';
        if (date != null) {
            return "TimeSlotReservation{" + '\'' +
                    base + '\'' +
                    "date=" + date + '\'' +
                    "}";
        } else if (group != null) {
            return "GroupReservation{" + '\'' +
                    base + '\'' +
                    "group=" + group + '\'' +
                    "}";
        } else if (skiPassType != null && duration != 0) {
            return "SkiPassReservation{" + '\'' +
                    base + '\'' +
                    "skiPassType=" + skiPassType + '\'' +
                    "duration=" + duration + '\'' +
                    "}";
        } else {
            return "Reservation{" + '\'' +
                    base + '\'' +
                    "}";
        }

    }
}
