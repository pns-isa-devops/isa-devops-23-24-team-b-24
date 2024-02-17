package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.ErrorDTO;
import teamb.w4e.entities.Advantage;
import teamb.w4e.entities.AdvantageType;
import teamb.w4e.interfaces.AdvantageFinder;
import teamb.w4e.interfaces.AdvantageRegistration;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = LeisureController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class LeisureController {

    public static final String BASE_URI = "/catalog";

    private final AdvantageRegistration registry;

    private final AdvantageFinder finder;

    @Autowired
    public LeisureController(AdvantageRegistration registry, AdvantageFinder finder) {
        this.registry = registry;
        this.finder = finder;
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
                    .body(registry.register(advantage.getName(), advantage.getType(), advantage.getPoints()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping(path = "/advantages")
    public ResponseEntity<List<Advantage>> advantages() {
        return ResponseEntity.ok(finder.findAll());
    }

    @GetMapping(path = "/advantages/types")
    public ResponseEntity<Set<AdvantageType>> showAdvantageTypes() {
        return ResponseEntity.ok(finder.listAdvantageTypes());
    }

    @GetMapping(path = "/advantages/types/{type}")
    public ResponseEntity<List<Advantage>> advantagesOfType(@PathVariable AdvantageType type) {
        return ResponseEntity.ok(finder.findByType(type).stream().toList());
    }
}
