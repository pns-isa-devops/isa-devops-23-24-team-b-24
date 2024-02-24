package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.CardDTO;
import teamb.w4e.dto.CustomerDTO;
import teamb.w4e.dto.ErrorDTO;
import teamb.w4e.dto.GroupDTO;
import teamb.w4e.entities.Card;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.exceptions.AlreadyExistingCustomerException;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.group.AlreadyLeaderException;
import teamb.w4e.exceptions.group.NotEnoughMembersException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.CustomerRegistration;
import teamb.w4e.interfaces.GroupCreator;
import teamb.w4e.interfaces.GroupFinder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CustomerCareController {

    public static final String BASE_URI = "/customers";

    private final CustomerRegistration registry;

    private final CustomerFinder customerFinder;

    private final GroupCreator createGroup;

    private final GroupFinder groupFinder;

    @Autowired
    public CustomerCareController(CustomerRegistration registry, CustomerFinder finder, GroupCreator createGroup, GroupFinder groupFinder) {
        this.registry = registry;
        this.customerFinder = finder;
        this.createGroup = createGroup;
        this.groupFinder = groupFinder;
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

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> register(@RequestBody @Valid CustomerDTO customerDTO) {
        // Note that there is no validation at all on the CustomerDto mapped
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertCustomerToDto(registry.register(customerDTO.name(), customerDTO.creditCard())));
        } catch (AlreadyExistingCustomerException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        return ResponseEntity.ok(customerFinder.findAll().stream().map(CustomerCareController::convertCustomerToDto).toList());
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") Long customerId) throws CustomerIdNotFoundException {
        return ResponseEntity.ok(convertCustomerToDto(customerFinder.retrieveCustomer(customerId)));
    }

    @PostMapping(path = "/groups/{customerId}/group", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDTO> createGroup(@PathVariable("customerId") Long customerId, @RequestBody @Valid GroupDTO groupDTO) throws CustomerIdNotFoundException, AlreadyLeaderException, NotEnoughMembersException {
        Customer leader = customerFinder.findById(customerId).orElseThrow(() -> new CustomerIdNotFoundException(customerId));
        Set<Customer> members = groupDTO.members().stream().map(customer -> {
            try {
                return customerFinder.findById(customer.id()).orElseThrow(() -> new CustomerIdNotFoundException(customer.id()));
            } catch (CustomerIdNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());


        return ResponseEntity.ok(convertGroupToDto(createGroup.createGroup(leader, members)));
    }

    @GetMapping(path = "/groups")
    public ResponseEntity<Set<GroupDTO>> getGroups() {
        return ResponseEntity.ok(groupFinder.findAll().stream().map(CustomerCareController::convertGroupToDto).collect(Collectors.toSet()));
    }



    public static CustomerDTO convertCustomerToDto(Customer customer) { // In more complex cases, we could use a ModelMapper such as MapStruct

        return new CustomerDTO(customer.getId(), customer.getName(), customer.getCreditCard(), convertCardToDto(customer.getCard()));
    }

    private static CardDTO convertCardToDto(Card card) { // In more complex cases, we could use a ModelMapper such as MapStruct
        return new CardDTO(card.getId());
    }

    public static GroupDTO convertGroupToDto(Group group) { // In more complex cases, we could use a ModelMapper such as MapStruct
        return new GroupDTO(group.getId(),CustomerCareController.convertCustomerToDto(group.getLeader()), group.getMembers().stream().map(CustomerCareController::convertCustomerToDto).collect(Collectors.toSet()));
    }
}

