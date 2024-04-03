package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.*;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughException;
import teamb.w4e.exceptions.group.NotInTheSameGroupException;
import teamb.w4e.interfaces.GroupCreator;
import teamb.w4e.interfaces.GroupFinder;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = GroupController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class GroupController {

    public static final String BASE_URI = "/customers/groups";
    private final GroupCreator createGroup;
    private final GroupFinder groupFinder;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotInTheSameGroupException.class})
    public ErrorDTO handleExceptions() {
        return new ErrorDTO("Trade failed : ", "The sender and receiver are not in the same group");
    }

    @Autowired
    public GroupController(GroupCreator createGroup, GroupFinder groupFinder) {
        this.createGroup = createGroup;
        this.groupFinder = groupFinder;
    }

    @PostMapping(path = "/{leaderId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDTO> createGroup(@PathVariable("leaderId") Long leaderId, @RequestBody @Valid GroupDTO groupDTO) throws IdNotFoundException, NotEnoughException {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertGroupToDto(createGroup.createGroup(leaderId, groupDTO.members().stream().map(CustomerDTO::id).collect(Collectors.toSet()))));
        } catch (AlreadyLeaderException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping(path = "/{leaderId}")
    public ResponseEntity<String> deleteGroup(@PathVariable("leaderId") Long leaderId) throws IdNotFoundException {
        Group group = groupFinder.retrieveGroup(leaderId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(createGroup.deleteGroup(leaderId));
    }

    @PostMapping(path = "/trades", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PointTransactionDTO> tradePoints(@RequestBody @Valid PointTradeDTO pointTradeDTO) throws IdNotFoundException, NotEnoughException, NotInTheSameGroupException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionController.convertPointTransactionToDto(createGroup.createTrade(pointTradeDTO.sender().id(), pointTradeDTO.receiver().id(), pointTradeDTO.points())));
    }

    @GetMapping(path = "/{leaderId}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable("leaderId") Long leaderId) throws IdNotFoundException {
        return ResponseEntity.ok(convertGroupToDto(groupFinder.retrieveGroup(leaderId)));
    }

    @GetMapping
    public ResponseEntity<Set<GroupDTO>> getGroups() {
        return ResponseEntity.ok(groupFinder.findAll().stream().map(GroupController::convertGroupToDto).collect(Collectors.toSet()));
    }

    public static GroupDTO convertGroupToDto(Group group) {
        return new GroupDTO(group.getId(), CustomerController.convertCustomerToDto(group.getLeader()), group.getMembers().stream().map(CustomerController::convertCustomerToDto).collect(Collectors.toSet()));
    }
}

