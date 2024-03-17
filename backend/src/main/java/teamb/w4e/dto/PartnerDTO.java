package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record PartnerDTO(
        Long id,
        @NotBlank(message = "the name of the partner should not be blank")
        String name,
        Set<LeisureDTO> leisure
) {

}
