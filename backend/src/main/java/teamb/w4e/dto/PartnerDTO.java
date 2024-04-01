package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PartnerDTO(
        Long id,
        @NotBlank(message = "the name of the partner should not be blank")
        String name,
        @NotNull(message = "the leisure of the partner should not be null")
        Set<LeisureDTO> leisure,
        @NotNull(message = "the advantages of the partner should not be null")
        Set<AdvantageDTO> advantages
) {

}
