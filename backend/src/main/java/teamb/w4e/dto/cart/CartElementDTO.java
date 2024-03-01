package teamb.w4e.dto.cart;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import teamb.w4e.dto.GroupDTO;
import teamb.w4e.dto.LeisureDTO;
import teamb.w4e.entities.reservations.ReservationType;

public class CartElementDTO {
    Long id;
    @Enumerated(EnumType.STRING)
    ReservationType type;
   @NotNull
   LeisureDTO truc;
    String date;
    GroupDTO group;
    String skiPassType;
    int duration;

    public CartElementDTO() {
    }

    public CartElementDTO(ReservationType type, LeisureDTO truc) {
        this.type = type;
        this.truc = truc;
    }

    public CartElementDTO(ReservationType type, LeisureDTO truc, String date) {
        this.type = type;
        this.truc = truc;
        this.date = date;
    }

    public CartElementDTO(ReservationType type, LeisureDTO activity, GroupDTO group) {
        this.type = type;
        this.truc = activity;
        this.group = group;
    }

    public CartElementDTO(ReservationType type, LeisureDTO activity, String skiPassType, int duration) {
        this.type = type;
        this.truc = activity;
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

    public LeisureDTO getActivity() {
        return truc;
    }

    public void setActivity(LeisureDTO activity) {
        this.truc = activity;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    @Override
    public String toString() {
        String base = "CartElementDTO{" +
                "id=" + id +
                ", type=" + type +
                ", activity=" + truc;
        if (date != null) {
            return "TimeSlotReservationDTO{" +
                    base +
                    ", date='" + date + '\'' +
                    '}';
        } else if (group != null) {
            return "GroupReservationDTO{" +
                    base +
                    ", group=" + group +
                    '}';
        } else if (skiPassType != null) {
            return "SkiPassReservationDTO{" +
                    base +
                    ", skiPassType=" + skiPassType +
                    ", skiPassDuration=" + duration +
                    '}';
        } else {
            return base + '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartElementDTO that)) return false;
        return type.equals(that.getType()) && truc.equals(that.getActivity()) && date.equals(that.getDate()) && group.equals(that.getGroup()) && skiPassType.equals(that.getSkiPassType()) && duration == that.getDuration();
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + truc.hashCode();
        return result;
    }

}
