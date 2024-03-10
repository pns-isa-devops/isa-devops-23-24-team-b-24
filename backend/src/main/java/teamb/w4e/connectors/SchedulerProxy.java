package teamb.w4e.connectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import teamb.w4e.connectors.externaldto.AvailabilityRequestDTO;
import teamb.w4e.connectors.externaldto.AvailabilityReceiptDTO;
import teamb.w4e.connectors.externaldto.BookedReceiptDTO;
import teamb.w4e.connectors.externaldto.BookedRequestDTO;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.interfaces.Scheduler;

@Component
public class SchedulerProxy implements Scheduler {

    @Value("${scheduler.host.baseurl}")
    private String schedulerHostandPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean checkAvailability(Activity activity, String date) {
        try {
            ResponseEntity<AvailabilityReceiptDTO> result = restTemplate.postForEntity(
                    schedulerHostandPort + "/ccavailabilities",
                    new AvailabilityRequestDTO(activity.getId(), date),
                    AvailabilityReceiptDTO.class
            );
            return result.getStatusCode().equals(HttpStatus.CREATED) && result.hasBody();
        }
        catch (RestClientResponseException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return false;
            }
            throw errorException;
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean reserve(Activity activity, String date) {
        try {
            ResponseEntity<BookedReceiptDTO> result = restTemplate.postForEntity(
                    schedulerHostandPort + "/ccavailabilities/ccbooked",
                    new BookedRequestDTO(activity.getId(), date),
                    BookedReceiptDTO.class
            );
            return result.getStatusCode().equals(HttpStatus.CREATED) && result.hasBody();
        }
        catch (RestClientResponseException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return false;
            }
            throw errorException;
        }
    }
}

