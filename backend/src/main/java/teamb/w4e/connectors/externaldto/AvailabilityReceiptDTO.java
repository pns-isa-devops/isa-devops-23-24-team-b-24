package teamb.w4e.connectors.externaldto;

// External DTO to receive an availability receipt from a successful POST availability request to the external Scheduler system

public record AvailabilityReceiptDTO(String payReceiptId, boolean isAvailable)
{
}
