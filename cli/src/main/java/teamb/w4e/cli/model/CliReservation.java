package teamb.w4e.cli.model;

public class CliReservation {
    private Long id;
    private ReservationType type;
    private CliLeisure leisure;
    private String date;
    private CliGroup group;
    private String skiPassType;
    private int duration;

    public CliReservation() {
    }

    public CliReservation(ReservationType type, CliLeisure leisure) {
        this.type = type;
        this.leisure = leisure;
    }

    public CliReservation(ReservationType type, CliLeisure leisure, String date) {
        this.type = type;
        this.leisure = leisure;
        this.date = date;
    }

    public CliReservation(ReservationType type, CliLeisure leisure, CliGroup group) {
        this.type = type;
        this.leisure = leisure;
        this.group = group;
    }

    public CliReservation(ReservationType type, CliLeisure leisure, String skiPassType, int duration) {
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
        String base = "id=" + id + '\'' +
                "type=" + type + '\'' +
                "activity=" + leisure + '\'';
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
                    "duration=" + duration + '\'' +
                    "}";
            default -> "Reservation{" + '\'' +
                    base + '\'' +
                    "}";
        };
            }
}
