package teamb.w4e.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GroupDTO(
        Long id,
        @NotNull CustomerDTO leader,
        @NotNull Set<CustomerDTO> members) {
}

