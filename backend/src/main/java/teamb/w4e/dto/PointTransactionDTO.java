package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PointTransactionDTO(
        Long id,
        @NotNull
        CustomerDTO customer,
        @Positive
        int points,
        @NotBlank(message = "Trade cannot be blank")
        String trade
) {
}
