package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.CustomerDTO;
import teamb.w4e.dto.ErrorDTO;
import teamb.w4e.dto.GroupDTO;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.AlreadyExistingCustomerException;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.CustomerRegistration;
import teamb.w4e.interfaces.GroupCreator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CustomerCareController {

    public static final String BASE_URI = "/customers";

    private final CustomerRegistration registry;

    private final CustomerFinder finder;

    private final GroupCreator createGroup;

    @Autowired
    public CustomerCareController(CustomerRegistration registry, CustomerFinder finder, GroupCreator createGroup) {
        this.registry = registry;
        this.finder = finder;
        this.createGroup = createGroup;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // The 422 (Unprocessable Entity) status code means the server understands the content type of the request entity
    // (hence a 415(Unsupported Media Type) status code is inappropriate), and the syntax of the request entity is
    // correct (thus a 400 (Bad Request) status code is inappropriate) but was unable to process the contained
    // instructions.
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Customer information", e.getMessage());
    }

    @PostMapping(path = "/register", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> register(@RequestBody @Valid CustomerDTO cusdto) {
        // Note that there is no validation at all on the CustomerDto mapped
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertCustomerToDto(registry.register(cusdto.name(), cusdto.creditCard())));
        } catch (AlreadyExistingCustomerException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        return ResponseEntity.ok(finder.findAll().stream().map(CustomerCareController::convertCustomerToDto).toList());
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") Long customerId) throws CustomerIdNotFoundException {
        return ResponseEntity.ok(convertCustomerToDto(finder.retrieveCustomer(customerId)));
    }

    private static CustomerDTO convertCustomerToDto(Customer customer) { // In more complex cases, we could use a ModelMapper such as MapStruct
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getCreditCard());
    }

    @PostMapping(path = "/{customerId}/group", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDTO> createGroup(@RequestBody @Valid GroupDTO groupDTO) {
        // Retriever the leader
        Customer leader = finder.findByName(groupDTO.leaderName()).get();

        // Retrieve  members
        Set<Customer> members = groupDTO.membersNames().stream()
                .map(finder::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(convertGroupToDto(createGroup.createGroup(leader, members)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    private static GroupDTO convertGroupToDto(Group group) { // In more complex cases, we could use a ModelMapper such as MapStruct
        return new GroupDTO(group.getId(), group.getLeader().getName(), group.getMembers().stream().map(Customer::getName).collect(Collectors.toSet()));
    }


}

