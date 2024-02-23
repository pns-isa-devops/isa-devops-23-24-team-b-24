package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.CartElementDTO;
import teamb.w4e.dto.ReservationDTO;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Item;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.ActivityFinder;
import teamb.w4e.interfaces.CartModifier;
import teamb.w4e.interfaces.CartProcessor;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CartController {

    public static final String CART_URI = "/{customerId}/cart";

    private final CartProcessor processor;
    private final CartModifier cart;

    private final ActivityFinder finder;


    @Autowired
    public CartController(CartProcessor processor, CartModifier cart, ActivityFinder finder) {
        this.processor = processor;
        this.cart = cart;
        this.finder = finder;
    }

    @PostMapping(path = CART_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CartElementDTO> updateCustomerCart(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartDTO) throws IdNotFoundException, NonValidDateForActivity, CustomerIdNotFoundException {
        Activity activity = finder.retrieveActivity(cartDTO.getActivity().id());
        return ResponseEntity.ok(convertCartElementToDTO(cart.update(customerId, activity, cartDTO.getDate())));
    }

    @GetMapping(path = CART_URI)
    public ResponseEntity<Set<CartElementDTO>> getCustomerCartContents(@PathVariable("customerId") Long customerId) throws IdNotFoundException, CustomerIdNotFoundException {
        return ResponseEntity.ok(cart.cartContent(customerId).stream().map(CartController::convertCartElementToDTO).collect(Collectors.toSet()));
    }

    @PostMapping(path = CART_URI + "/reservation", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> reserve(@PathVariable("customerId") Long customerId, @RequestBody @Valid Item item) throws EmptyCartException, PaymentException, IdNotFoundException, CustomerIdNotFoundException, NegativeAmountTransactionException {
        return ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validate(customerId, item)));
    }

    private static CartElementDTO convertCartElementToDTO(Item item) {
        return new CartElementDTO(LeisureController.convertActivityToDto(item.getActivity()), item.getDate());
    }

}
