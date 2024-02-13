package teamb.w4e.dto;

import jakarta.validation.constraints.NotBlank;

public record TransactionDTO(
        Long id,
        @NotBlank(message = "name should not be blank") String customerName,
        @NotBlank(message = "amount should not be blank") Double amount,
        @NotBlank(message = "paymentId should not be blank") String paymentId)
{
}
