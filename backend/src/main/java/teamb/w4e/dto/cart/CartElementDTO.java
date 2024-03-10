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
   LeisureDTO leisure;
    String date;
    GroupDTO group;
    String skiPassType;
    int duration;

    public CartElementDTO() {
    }

    public CartElementDTO(ReservationType type, LeisureDTO leisure) {
        this.type = type;
        this.leisure = leisure;
    }

    public CartElementDTO(ReservationType type, LeisureDTO leisure, String date) {
        this.type = type;
        this.leisure = leisure;
        this.date = date;
    }

    public CartElementDTO(ReservationType type, LeisureDTO leisure, GroupDTO group) {
        this.type = type;
        this.leisure = leisure;
        this.group = group;
    }

    public CartElementDTO(ReservationType type, LeisureDTO leisure, String skiPassType, int duration) {
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

    public LeisureDTO getLeisure() {
        return leisure;
    }

    public void setLeisure(LeisureDTO leisure) {
        this.leisure = leisure;
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
        String base =
                "id=" + id +
                ", type=" + type +
                ", leisure=" + leisure;
        if (date != null) {
            return "TimeSlotElementDTO{" +
                    base +
                    ", date='" + date + '\'' +
                    '}';
        } else if (group != null) {
            return "GroupElementDTO{" +
                    base +
                    ", group=" + group +
                    '}';
        } else if (skiPassType != null) {
            return "SkiPassElementDTO{" +
                    base +
                    ", skiPassType=" + skiPassType +
                    ", skiPassDuration=" + duration +
                    '}';
        } else {
            return "ServiceElementDTO{" +
                    base +
                    "}";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartElementDTO that)) return false;
        return type.equals(that.getType()) && leisure.equals(that.getLeisure()) && date.equals(that.getDate()) && group.equals(that.getGroup()) && skiPassType.equals(that.getSkiPassType()) && duration == that.getDuration();
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + leisure.hashCode();
        return result;
    }

}
