package teamb.w4e.dto;

import java.util.Set;

public record GroupDTO(
        Long id,
        CustomerDTO leader,
        Set<CustomerDTO> members) {
}

