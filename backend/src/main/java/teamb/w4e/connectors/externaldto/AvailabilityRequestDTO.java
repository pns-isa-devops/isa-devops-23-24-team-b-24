package teamb.w4e.connectors.externaldto;

// External DTO (Data Transfert Object) to POST availability request to the external Scheduler system

import jakarta.validation.constraints.Pattern;

public record AvailabilityRequestDTO(
        Long id,
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2]) (?:[01]\\d|2[0-3]):(?:[0-5]\\d)")
        String date) {
}
