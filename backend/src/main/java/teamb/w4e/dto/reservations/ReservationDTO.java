package teamb.w4e.dto.reservations;

import teamb.w4e.dto.GroupDTO;
import teamb.w4e.dto.LeisureDTO;
import teamb.w4e.entities.reservations.ReservationType;

public class ReservationDTO {
    Long id;
    ReservationType type;
    LeisureDTO leisure;
    String date;
    GroupDTO group;
    String skiPassType;
    int skiPassDuration;

    public ReservationDTO() {
    }

    public ReservationDTO(Long id, ReservationType type, LeisureDTO leisure) {
        this.id = id;
        this.type = type;
        this.leisure = leisure;
    }

    public ReservationDTO(Long id, ReservationType type, LeisureDTO leisure, String date) {
        this.id = id;
        this.type = type;
        this.leisure = leisure;
        this.date = date;
    }

    public ReservationDTO(Long id, ReservationType type, LeisureDTO leisure, GroupDTO group) {
        this.id = id;
        this.type = type;
        this.leisure = leisure;
        this.group = group;
    }

    public ReservationDTO(Long id, ReservationType type, LeisureDTO leisure, String skiPassType, int skiPassDuration) {
        this.id = id;
        this.type = type;
        this.leisure = leisure;
        this.skiPassType = skiPassType;
        this.skiPassDuration = skiPassDuration;
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

    public LeisureDTO getActivity() {
        return leisure;
    }

    public void setActivity(LeisureDTO activity) {
        this.leisure = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
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
                "activity=" + leisure + '\'';
        if (date != null) {
            return "TimeSlotReservationDTO{" + '\'' +
                    base + '\'' +
                    "date=" + date + '\'' +
                    "}";
        } else if (group != null) {
            return "GroupReservationDTO{" + '\'' +
                    base + '\'' +
                    "group=" + group + '\'' +
                    "}";
        } else if (skiPassType != null && skiPassDuration != 0) {
            return "SkiPassReservationDTO{" + '\'' +
                    base + '\'' +
                    "skiPassType=" + skiPassType + '\'' +
                    "skiPassDuration=" + skiPassDuration + '\'' +
                    "}";
        } else {
            return "ReservationDTO{" + '\'' +
                    base + '\'' +
                    "}";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationDTO that)) return false;
        return id.equals(that.id) && type.equals(that.type) && leisure.equals(that.leisure) && date.equals(that.date) && group.equals(that.group) && skiPassType.equals(that.skiPassType) && skiPassDuration == that.skiPassDuration;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + leisure.hashCode();
        return result;
    }

}
