package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record GroupDTO(
        Long id,
        CustomerDTO leader,
        Set<CustomerDTO> members) {
}

