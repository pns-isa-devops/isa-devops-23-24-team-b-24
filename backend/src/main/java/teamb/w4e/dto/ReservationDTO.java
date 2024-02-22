package teamb.w4e.dto;

import jakarta.validation.constraints.Pattern;

public record ReservationDTO(
        Long id,
        ActivityDTO activity,
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2]) (?:[01]\\d|2[0-3]):(?:[0-5]\\d)")
        String date) {

}
