package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record ActivityDTO(
        Long id,
        @NotBlank(message = "name should not be blank") String name,
        @NotBlank(message = "name should not be blank") String description,
        Set<AdvantageDTO> advantages
) {
}
