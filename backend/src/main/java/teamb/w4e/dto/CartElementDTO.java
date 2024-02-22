package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CartElementDTO {

    ActivityDTO activity;
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2]) (?:[01]\\d|2[0-3]):(?:[0-5]\\d)")
    String date;

    public CartElementDTO() {
    }

    public CartElementDTO(ActivityDTO activity, String date) {
        this.activity = activity;
        this.date = date;
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

    @Override
    public String toString() {
        return "CartElementDTO{" + '\'' +
                "activity=" + activity + '\'' +
                ", date=" + date + '\'' +
                "}";
    }
}
