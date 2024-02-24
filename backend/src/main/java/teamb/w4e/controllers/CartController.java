package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.cart.CartElementDTO;
import teamb.w4e.dto.reservations.ReservationDTO;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.ActivityFinder;
import teamb.w4e.interfaces.CartModifier;
import teamb.w4e.interfaces.CartProcessor;
import teamb.w4e.interfaces.GroupFinder;

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
    private final GroupFinder groupFinder;


    @Autowired
    public CartController(CartProcessor processor, CartModifier cart, ActivityFinder finder, GroupFinder groupFinder) {
        this.processor = processor;
        this.cart = cart;
        this.finder = finder;
        this.groupFinder = groupFinder;
    }

    @PostMapping(path = CART_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CartElementDTO> updateCustomerCart(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartDTO) throws IdNotFoundException, NonValidDateForActivity, CustomerIdNotFoundException {
        Activity activity = finder.retrieveActivity(cartDTO.getActivity().id());
        if (cartDTO.getType().equals(ReservationType.TIME_SLOT)) {
            return ResponseEntity.ok(convertTimeSlotElementToDTO(cart.timeSlotUpdate(customerId, activity, cartDTO.getDate())));
        }
        Group group = groupFinder.retrieveGroup(customerId);
        return ResponseEntity.ok(convertCartGroupElementToDTO(cart.groupUpdate(customerId, activity, group)));
    }

    @GetMapping(path = CART_URI)
    public ResponseEntity<Set<CartElementDTO>> getCustomerCartContents(@PathVariable("customerId") Long customerId) throws IdNotFoundException, CustomerIdNotFoundException {
        Set<CartElementDTO> cartElements = cart.cartContent(customerId).stream().map(item -> {
            if (item.getType().equals(ReservationType.TIME_SLOT)) {
                return convertTimeSlotElementToDTO((TimeSlotItem) item);
            }
            return convertCartGroupElementToDTO((GroupItem) item);
        }).collect(Collectors.toSet());
        return ResponseEntity.ok(cartElements);
    }

    @PostMapping(path = CART_URI + "/reservation", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> reserve(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartElementDTO) throws EmptyCartException, PaymentException, IdNotFoundException, CustomerIdNotFoundException, NegativeAmountTransactionException {
        if (cartElementDTO.getType().equals(ReservationType.TIME_SLOT)) {
            return ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validate(customerId, new TimeSlotItem(finder.retrieveActivity(cartElementDTO.getActivity().id()), cartElementDTO.getDate()))));
        }
        return ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validate(customerId, new GroupItem(finder.retrieveActivity(cartElementDTO.getActivity().id()), groupFinder.retrieveGroup(customerId)))));
    }


    private static CartElementDTO convertTimeSlotElementToDTO(TimeSlotItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto(item.getActivity()), item.getTimeSlot());
    }

    private static CartElementDTO convertCartGroupElementToDTO(GroupItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto(item.getActivity()), CustomerCareController.convertGroupToDto(item.getGroup()));
    }

}
