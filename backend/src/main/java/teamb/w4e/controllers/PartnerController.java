package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.ErrorDTO;
import teamb.w4e.dto.LeisureDTO;
import teamb.w4e.dto.PartnerDTO;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Leisure;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.PartnerFinder;
import teamb.w4e.interfaces.PartnerRegistration;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = PartnerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class PartnerController {

    public static final String BASE_URI = "/partners";

    private final PartnerRegistration registry;

    private final PartnerFinder finder;

    @Autowired
    public PartnerController(PartnerRegistration registry, PartnerFinder finder) {
        this.registry = registry;
        this.finder = finder;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // The 422 (Unprocessable Entity) status code means the server understands the content type of the request entity
    // (hence a 415(Unsupported Media Type) status code is inappropriate), and the syntax of the request entity is
    // correct (thus a 400 (Bad Request) status code is inappropriate) but was unable to process the contained
    // instructions.
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Partner information", e.getMessage());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerDTO> registerPartner(@RequestBody @Valid PartnerDTO partnerDTO) {
        // Note that there is no validation at all on the CustomerDto mapped
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertPartnerToDto(registry.register(partnerDTO.name())));
        } catch (AlreadyExistingException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getPartners() {
        return ResponseEntity.ok(finder.findAll().stream().map(PartnerController::convertPartnerToDto).toList());
    }

    @GetMapping(path = "/{partnerId}")
    public ResponseEntity<PartnerDTO> getPartner(@PathVariable("partnerId") Long partnerId) throws IdNotFoundException {
        return ResponseEntity.ok(convertPartnerToDto(finder.retrievePartner(partnerId)));
    }

    private static PartnerDTO convertPartnerToDto(Partner partner) {
        return new PartnerDTO(partner.getId(), partner.getName(), partner.getLeisure().stream().map(PartnerController::convertLeisureToDto).collect(Collectors.toSet()));
    }

    public static LeisureDTO convertLeisureToDto(Leisure leisure) {
        return new LeisureDTO(leisure.getId(), leisure.getName(), leisure.getDescription(), leisure.getPrice(), leisure.isBooked(), leisure.getAdvantages().stream().map(LeisureController::convertAdvantageToDto).collect(Collectors.toSet()));
    }
}
