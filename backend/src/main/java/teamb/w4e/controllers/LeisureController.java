package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.ActivityDTO;
import teamb.w4e.dto.ErrorDTO;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Advantage;
import teamb.w4e.entities.AdvantageType;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.ActivityFinder;
import teamb.w4e.interfaces.ActivityRegistration;
import teamb.w4e.interfaces.AdvantageFinder;
import teamb.w4e.interfaces.AdvantageRegistration;

import java.util.HashSet;
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
    private final AdvantageFinder advantageFinder;
    private final ActivityFinder activityFinder;

    @Autowired
    public LeisureController(AdvantageRegistration advantageRegistry, AdvantageFinder advantageFinder, ActivityRegistration activityRegistry, ActivityFinder activityFinder) {
        this.advantageRegistry = advantageRegistry;
        this.activityRegistry = activityRegistry;
        this.advantageFinder = advantageFinder;
        this.activityFinder = activityFinder;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Advantage information", e.getMessage());
    }

    @PostMapping(path = "/advantages", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Advantage> register(@RequestBody @Valid Advantage advantage) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(advantageRegistry.register(advantage.getName(), advantage.getType(), advantage.getPoints()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping(path = "/advantages")
    public ResponseEntity<List<Advantage>> advantages() {
        return ResponseEntity.ok(advantageFinder.findAllAdvantages());
    }

    @GetMapping(path = "/advantages/types")
    public ResponseEntity<Set<AdvantageType>> showAdvantageTypes() {
        return ResponseEntity.ok(advantageFinder.listAdvantageTypes());
    }

    @GetMapping(path = "/advantages/types/{type}")
    public ResponseEntity<List<Advantage>> advantagesOfType(@PathVariable AdvantageType type) {
        return ResponseEntity.ok(advantageFinder.findByType(type).stream().toList());
    }

    @PostMapping(path = "/activities", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> register(@RequestBody @Valid ActivityDTO activityDTO) {
        Set<Advantage> advantages = activityDTO.advantages().stream()
                .map(advantage -> advantageFinder.findByName(advantage.getName()).orElseThrow())
                .collect(Collectors.toSet());
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertActivityToDto(activityRegistry.register(activityDTO.name(), activityDTO.description(), advantages)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

    }

    @GetMapping(path = "/activities")
    public ResponseEntity<List<ActivityDTO>> activities() {
        return ResponseEntity.ok(activityFinder.findAllActivities().stream().map(LeisureController::convertActivityToDto).toList());
    }

    @GetMapping(path = "/activities/{activityId}")
    public ResponseEntity<ActivityDTO> getActivity(@PathVariable Long activityId) throws IdNotFoundException {
        return ResponseEntity.ok(convertActivityToDto(activityFinder.retrieveActivity(activityId)));
    }

    private static ActivityDTO convertActivityToDto(Activity activity) {
        return new ActivityDTO(activity.getId(), activity.getName(), activity.getDescription(), new HashSet<>(activity.getAdvantages()));
    }

}
