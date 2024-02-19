package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.CartElementDTO;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Item;
import teamb.w4e.exceptions.EmptyCartException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NonValidDateForActivity;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.ActivityFinder;
import teamb.w4e.interfaces.CartModifier;
import teamb.w4e.interfaces.CartProcessor;

import java.util.Date;
import java.util.Set;

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
    public ResponseEntity<CartElementDTO> updateCustomerCart(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartDTO) throws IdNotFoundException, NonValidDateForActivity {
        Activity activity = finder.findActivityById(cartDTO.getActivity().id()).orElseThrow(() -> new IdNotFoundException(cartDTO.getActivity().id()));
        return ResponseEntity.ok(convertToCartElementDTO(cart.update(customerId, activity, cartDTO.getDate())));
    }

    @GetMapping(path = CART_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Item>> getCustomerCartContents(@PathVariable("customerId") Long customerId) throws IdNotFoundException {
        return ResponseEntity.ok(cart.cartContent(customerId));
    }

//    @PostMapping(path = CART_URI + "/validate", consumes = APPLICATION_JSON_VALUE)
//    public ResponseEntity<OrderDTO> reserve(@PathVariable("customerId") Long customerId) throws EmptyCartException, PaymentException, IdNotFoundException {
//        return ResponseEntity.ok().body(OrderController.convertOrderToDto(processor.validate(customerId)));
//    }

    private static CartElementDTO convertToCartElementDTO(Item item) {
        return new CartElementDTO(LeisureController.convertActivityToDto(item.getActivity()), item.getDate());
    }

}
