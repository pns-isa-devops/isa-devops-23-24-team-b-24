package teamb.w4e.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PointTradeDTO(
        @NotNull
        CustomerDTO sender,
        @NotNull
        CustomerDTO receiver,
        @Positive
        int points
) {
}
