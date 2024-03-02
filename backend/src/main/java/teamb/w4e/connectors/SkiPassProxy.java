package teamb.w4e.connectors;

import teamb.w4e.connectors.externaldto.PaymentReceiptDTO;
import teamb.w4e.connectors.externaldto.PaymentRequestDTO;
import teamb.w4e.connectors.externaldto.SkiPassReservationReceiptDTO;
import teamb.w4e.connectors.externaldto.SkiPassReservationRequestDTO;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.SkiPass;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class SkiPassProxy implements SkiPass {

    @Value("${skipass.host.baseurl}")
    private String skipassHostandPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<String> reserve(Customer customer, Activity activity) {
        try {
            ResponseEntity<SkiPassReservationReceiptDTO> result = restTemplate.postForEntity(
                    skipassHostandPort + "/spreservations",
                    new SkiPassReservationRequestDTO(customer, activity),
                    PaymentReceiptDTO.class
            );
            if (result.getStatusCode().equals(HttpStatus.CREATED) && result.hasBody()) {
                return Optional.of(result.getBody().payReceiptId());
            } else {
                return Optional.empty();
            }
        }
        catch (RestClientResponseException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return Optional.empty();
            }
            throw errorException;
        }
    }

}
