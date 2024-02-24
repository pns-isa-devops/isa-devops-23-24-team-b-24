package teamb.w4e.dto.reservations;

import teamb.w4e.dto.ActivityDTO;
import teamb.w4e.dto.GroupDTO;
import teamb.w4e.entities.reservations.ReservationType;

public class ReservationDTO {
    Long id;
    ReservationType type;
    ActivityDTO activity;

    String date;

    GroupDTO group;

    public ReservationDTO() {
    }

    public ReservationDTO(Long id, ReservationType type, ActivityDTO activity) {
        this.id = id;
        this.type = type;
        this.activity = activity;
    }

    public ReservationDTO(Long id, ReservationType type, ActivityDTO activity, String date) {
        this.id = id;
        this.type = type;
        this.activity = activity;
        this.date = date;
    }

    public ReservationDTO(Long id, ReservationType type, ActivityDTO activity, GroupDTO group) {
        this.id = id;
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

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
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

    @Override
    public String toString() {
        if (date != null) {
            return "ReservationDTO{" + '\'' +
                    "id=" + id + '\'' +
                    "type=" + type + '\'' +
                    "activity=" + activity + '\'' +
                    "date=" + date + '\'' +
                    "}";
        } else if (group != null) {
            return "ReservationDTO{" + '\'' +
                    "id=" + id + '\'' +
                    "type=" + type + '\'' +
                    "activity=" + activity + '\'' +
                    "group=" + group + '\'' +
                    "}";
        } else {
            return "ReservationDTO{" + '\'' +
                    "id=" + id + '\'' +
                    "type=" + type + '\'' +
                    "activity=" + activity + '\'' +
                    "}";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationDTO that)) return false;
        return id.equals(that.id) && type.equals(that.type) && activity.equals(that.activity) && date.equals(that.date) && group.equals(that.group);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + activity.hashCode();
        return result;
    }

}
