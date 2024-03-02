package teamb.w4e.connectors.externaldto;

// External DTO to receive a payment receipt from a successful POST payment request to the external Bank system
public record SkiPassReservationReceiptDTO (String payReceiptId, double amount)
{
}

