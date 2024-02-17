package teamb.w4e.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionDTO(
        Long id,
        @NotNull CustomerDTO customer,
        @Positive double amount) {
}
