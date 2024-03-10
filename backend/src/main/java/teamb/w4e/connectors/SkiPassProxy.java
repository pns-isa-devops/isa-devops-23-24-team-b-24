package teamb.w4e.connectors;

import teamb.w4e.connectors.externaldto.PaymentReceiptDTO;
import teamb.w4e.connectors.externaldto.PaymentRequestDTO;
import teamb.w4e.connectors.externaldto.ReservationReceiptDTO;
import teamb.w4e.connectors.externaldto.ReservationRequestDTO;
import teamb.w4e.entities.Customer;
import teamb.w4e.interfaces.Bank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.interfaces.SkiPass;

import java.util.Optional;

@Component
public class SkiPassProxy implements SkiPass {

    //TODO : include the correct URL
    @Value("${skipass.host.baseurl}")
    private String skipassHostedPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<String> reserve(String name, String activity, int duration) {
        try {
            ResponseEntity<ReservationReceiptDTO> result = restTemplate.postForEntity(
                    skipassHostedPort + "/ccskipass",
                    new ReservationRequestDTO(name, activity, duration),
                    ReservationReceiptDTO.class
            );
            if (result.getStatusCode().equals(HttpStatus.CREATED) && result.hasBody()) {
                return Optional.of(result.getBody().reservationReceiptId());
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
