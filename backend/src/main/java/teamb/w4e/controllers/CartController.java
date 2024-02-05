package teamb.w4e.controllers;

import teamb.w4e.dto.OrderDTO;
import teamb.w4e.entities.Item;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.EmptyCartException;
import teamb.w4e.exceptions.NegativeQuantityException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.CartModifier;
import teamb.w4e.interfaces.CartProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
// referencing the same BASE_URI as Customer care to extend it hierarchically
public class CartController {

    public static final String CART_URI = "/{customerId}/cart";

    private final CartModifier cart;

    private final CartProcessor processor;

    @Autowired
    public CartController(CartModifier cart, CartProcessor processor) {
        this.cart = cart;
        this.processor = processor;
    }

    @PostMapping(path = CART_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> updateCustomerCart(@PathVariable("customerId") Long customerId, @RequestBody Item it) throws CustomerIdNotFoundException, NegativeQuantityException {
        return ResponseEntity.ok(cart.update(customerId, it)); // Item is used as a DTO in and out here...
    }

    @GetMapping(CART_URI)
    public ResponseEntity<Set<Item>> getCustomerCartContents(@PathVariable("customerId") Long customerId) throws CustomerIdNotFoundException {
        return ResponseEntity.ok(cart.cartContent(customerId));
    }

    @PostMapping(path = CART_URI + "/validate")
    public ResponseEntity<OrderDTO> validate(@PathVariable("customerId") Long customerId) throws EmptyCartException, PaymentException, CustomerIdNotFoundException {
        return ResponseEntity.ok().body(OrderController.convertOrderToDto(processor.validate(customerId)));
    }

}
