package teamb.w4e.dto;

import jakarta.validation.constraints.NotNull;

public record AppliedAdvantageDTO(
        @NotNull
        AdvantageDTO advantage,
        @NotNull
        LeisureDTO leisure
) {
}
