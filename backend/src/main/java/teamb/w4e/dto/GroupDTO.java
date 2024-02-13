package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record GroupDTO(
        Long id,
        @NotBlank(message = "name should not be blank") String leaderName,
        Set<@NotBlank(message = "name should not be blank") String> membersNames) {
}

