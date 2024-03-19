package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record LeisureDTO(
        Long id,
        String partnerName,
        @NotBlank(message = "name should not be blank")
        String name,
        @NotBlank(message = "description should not be blank")
        String description,
        @Positive(message = "price should be positive")
        double price,
        boolean booked, // activity (true) service (false)
        Set<AdvantageDTO> advantages
) {
}
