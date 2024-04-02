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
import teamb.w4e.entities.customers.Card;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.CustomerRegistration;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CustomerController {

    public static final String BASE_URI = "/customers";
    private final CustomerRegistration registry;

    private final CustomerFinder customerFinder;

    @Autowired
    public CustomerController(CustomerRegistration registry, CustomerFinder finder) {
        this.registry = registry;
        this.customerFinder = finder;
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
        } catch (AlreadyExistingException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        return ResponseEntity.ok(customerFinder.findAll().stream().map(CustomerController::convertCustomerToDto).toList());
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") Long customerId) {
        try {
            return ResponseEntity.ok(convertCustomerToDto(customerFinder.retrieveCustomer(customerId)));
        } catch (IdNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    public static CustomerDTO convertCustomerToDto(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getCreditCard(), convertCardToDto(customer.getCard()));
    }

    private static CardDTO convertCardToDto(Card card) {
        return new CardDTO(card.getId());
    }
}

