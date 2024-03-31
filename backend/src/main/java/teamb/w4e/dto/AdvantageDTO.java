package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import teamb.w4e.entities.catalog.AdvantageType;

public record AdvantageDTO(
        Long id,
        @NotBlank(message = "name should not be blank") String name,
        AdvantageType type,
        @Positive int points,
        @NotBlank String partner) {
}
