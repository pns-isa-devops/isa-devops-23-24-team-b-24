package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.*;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.leisure.ActivityFinder;
import teamb.w4e.interfaces.leisure.ActivityRegistration;
import teamb.w4e.interfaces.AdvantageFinder;
import teamb.w4e.interfaces.AdvantageRegistration;
import teamb.w4e.interfaces.leisure.ServiceFinder;
import teamb.w4e.interfaces.leisure.ServiceRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = LeisureController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class LeisureController {

    public static final String BASE_URI = "/catalog";

    private final AdvantageRegistration advantageRegistry;
    private final ActivityRegistration activityRegistry;
    private final ServiceRegistration serviceRegistry;
    private final AdvantageFinder advantageFinder;
    private final ActivityFinder activityFinder;
    private final ServiceFinder serviceFinder;

    @Autowired
    public LeisureController(AdvantageRegistration advantageRegistry, AdvantageFinder advantageFinder, ActivityRegistration activityRegistry, ServiceRegistration serviceRegistry, ActivityFinder activityFinder, ServiceFinder serviceFinder) {
        this.advantageRegistry = advantageRegistry;
        this.activityRegistry = activityRegistry;
        this.advantageFinder = advantageFinder;
        this.serviceRegistry = serviceRegistry;
        this.activityFinder = activityFinder;
        this.serviceFinder = serviceFinder;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Advantage information", e.getMessage());
    }

    @PostMapping(path = "/advantages", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdvantageDTO> register(@RequestBody @Valid AdvantageDTO advantage) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertAdvantageToDto(advantageRegistry.register(advantage.name(), advantage.type(), advantage.points())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping(path = "/advantages")
    public ResponseEntity<List<AdvantageDTO>> advantages() {
        return ResponseEntity.ok(advantageFinder.findAllAdvantages().stream().map(LeisureController::convertAdvantageToDto).toList());
    }

    @GetMapping(path = "/advantages/{advantageId}")
    public ResponseEntity<AdvantageDTO> getAdvantage(@PathVariable Long advantageId) throws IdNotFoundException {
        return ResponseEntity.ok(convertAdvantageToDto(advantageFinder.retrieveAdvantage(advantageId)));
    }

    @GetMapping(path = "/advantages/types")
    public ResponseEntity<Set<AdvantageType>> showAdvantageTypes() {
        return ResponseEntity.ok(advantageFinder.listAdvantageTypes());
    }

    @GetMapping(path = "/advantages/types/{type}")
    public ResponseEntity<List<AdvantageDTO>> advantagesOfType(@PathVariable AdvantageType type) {
        return ResponseEntity.ok(advantageFinder.findByType(type).stream().map(LeisureController::convertAdvantageToDto).toList());
    }

    @PostMapping(path = "/activities", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeisureDTO> registerActivity(@RequestBody @Valid LeisureDTO activityDTO) {
        Set<Advantage> advantages = activityDTO.advantages().stream()
                .map(advantage -> advantageFinder.findByName(advantage.name()).orElseThrow())
                .collect(Collectors.toSet());
        try {
            Activity activity = activityRegistry.register(activityDTO.name(), activityDTO.description(), activityDTO.price(), advantages);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertActivityToDto(activityFinder.retrieveActivity(activity.getId())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping(path = "/activities")
    public ResponseEntity<List<LeisureDTO>> activities() {
        return ResponseEntity.ok(activityFinder.findAllActivities().stream().map(LeisureController::convertActivityToDto).toList());
    }

    @GetMapping(path = "/activities/{activityId}")
    public ResponseEntity<LeisureDTO> getActivity(@PathVariable Long activityId) throws IdNotFoundException {
        return ResponseEntity.ok(convertActivityToDto(activityFinder.retrieveActivity(activityId)));
    }

    @PostMapping(path = "/services", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeisureDTO> registerService(@RequestBody @Valid LeisureDTO serviceDTO) {
        Set<Advantage> advantages = serviceDTO.advantages().stream()
                .map(advantage -> advantageFinder.findByName(advantage.name()).orElseThrow())
                .collect(Collectors.toSet());
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertServiceToDto(serviceRegistry.registerService(serviceDTO.name(), serviceDTO.description(), serviceDTO.price(), advantages)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping(path = "/services")
    public ResponseEntity<List<LeisureDTO>> services() {
        return ResponseEntity.ok(serviceFinder.findAllServices().stream().map(LeisureController::convertServiceToDto).toList());
    }

    @GetMapping(path = "/services/{serviceId}")
    public ResponseEntity<LeisureDTO> getService(@PathVariable Long serviceId) throws IdNotFoundException {
        return ResponseEntity.ok(convertServiceToDto(serviceFinder.retrieveService(serviceId)));
    }

    @GetMapping()
    public ResponseEntity<List<LeisureDTO>> getAllLeisure() {
        List<LeisureDTO> leisure = new ArrayList<>();
        leisure.addAll(activityFinder.findAllActivities().stream().map(LeisureController::convertActivityToDto).toList());
        leisure.addAll(serviceFinder.findAllServices().stream().map(LeisureController::convertServiceToDto).toList());
        return ResponseEntity.ok(leisure);
    }

    @DeleteMapping(path = BASE_URI + "/{leisureId}/delete")
    public ResponseEntity<String> deleteLeisure(@PathVariable Long leisureId) {
        try {
            activityFinder.deleteActivity(leisureId);
            return ResponseEntity.ok("Leisure " + leisureId + " removed");
        } catch (IdNotFoundException e) {
            try {
                serviceFinder.deleteService(leisureId);
                return ResponseEntity.ok("Leisure " + leisureId + " removed");
            } catch (IdNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leisure " + leisureId + " not found");
            }
        }
    }

    private static AdvantageDTO convertAdvantageToDto(Advantage advantage) {
        return new AdvantageDTO(advantage.getId(), advantage.getName(), advantage.getType(), advantage.getPoints());
    }

    public static LeisureDTO convertActivityToDto(Activity activity) {
        return new LeisureDTO(activity.getId(), activity.getName(), activity.getDescription(), activity.getPrice(), true, activity.getAdvantages().stream().map(LeisureController::convertAdvantageToDto).collect(Collectors.toSet()));
    }

    public static LeisureDTO convertServiceToDto(Service service) {
        return new LeisureDTO(service.getId(), service.getName(), service.getDescription(), service.getPrice(), false, service.getAdvantages().stream().map(LeisureController::convertAdvantageToDto).collect(Collectors.toSet()));
    }

}
