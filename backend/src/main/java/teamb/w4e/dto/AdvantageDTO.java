package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import teamb.w4e.entities.AdvantageType;

public record AdvantageDTO(
        Long id,
        @NotBlank(message = "name should not be blank") String name,
        @NotEmpty AdvantageType type,
        @Positive int points) {
}
