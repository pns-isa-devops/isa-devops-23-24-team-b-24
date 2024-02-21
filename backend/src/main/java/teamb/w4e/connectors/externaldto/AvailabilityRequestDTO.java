package teamb.w4e.connectors.externaldto;

// External DTO (Data Transfert Object) to POST availability request to the external Scheduler system

import java.util.Date;

public record AvailabilityRequestDTO(Long id, Date date) {
}
