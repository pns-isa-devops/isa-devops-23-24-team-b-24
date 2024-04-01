package teamb.w4e.entities.reservations;

public enum ReservationType {
    GROUP("group"),
    TIME_SLOT("time_slot"),
    SKI_PASS("ski_pass"),
    SERVICE("service"),
    NONE("none");


    private final String type;

    ReservationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
