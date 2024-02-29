package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.cart.CartElementDTO;
import teamb.w4e.dto.reservations.ReservationDTO;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.SkiPassItem;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.CartModifier;
import teamb.w4e.interfaces.CartProcessor;
import teamb.w4e.interfaces.GroupFinder;
import teamb.w4e.interfaces.leisure.ActivityFinder;

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
        ReservationType type = cartDTO.getType();
        return switch (type) {
            case TIME_SLOT -> ResponseEntity.ok(convertTimeSlotElementToDTO(cart.timeSlotUpdate(customerId, activity, cartDTO.getDate())));
            case GROUP -> ResponseEntity.ok(convertCartGroupElementToDTO(cart.groupUpdate(customerId, activity, groupFinder.retrieveGroup(customerId))));
            case SKI_PASS -> ResponseEntity.ok(convertSkiPassElementToDTO(cart.skiPassUpdate(customerId, activity, cartDTO.getSkiPassType(), cartDTO.getSkiPassDuration())));
        };
    }

    @GetMapping(path = CART_URI)
    public ResponseEntity<Set<CartElementDTO>> getCustomerCartContents(@PathVariable("customerId") Long customerId) throws IdNotFoundException, CustomerIdNotFoundException {
        Set<CartElementDTO> cartElements = cart.cartContent(customerId).stream().map(item -> {
            ReservationType type = item.getType();
            return switch (type) {
                case TIME_SLOT -> convertTimeSlotElementToDTO((TimeSlotItem) item);
                case GROUP -> convertCartGroupElementToDTO((GroupItem) item);
                case SKI_PASS -> convertSkiPassElementToDTO((SkiPassItem) item);
            };
        }).collect(Collectors.toSet());
        return ResponseEntity.ok(cartElements);
    }

    @PostMapping(path = CART_URI + "/reservation", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> reserve(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartElementDTO) throws EmptyCartException, PaymentException, IdNotFoundException, CustomerIdNotFoundException, NegativeAmountTransactionException {
        ReservationType type = cartElementDTO.getType();
        return switch (type) {
            case TIME_SLOT ->
                    ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validate(customerId, new TimeSlotItem(finder.retrieveActivity(cartElementDTO.getActivity().id()), cartElementDTO.getDate()))));
            case GROUP ->
                    ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validate(customerId, new GroupItem(finder.retrieveActivity(cartElementDTO.getActivity().id()), groupFinder.retrieveGroup(customerId)))));
            case SKI_PASS ->
                    ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validate(customerId, new SkiPassItem(finder.retrieveActivity(cartElementDTO.getActivity().id()), cartElementDTO.getSkiPassType(), cartElementDTO.getSkiPassDuration()))));
        };
    }


    private static CartElementDTO convertTimeSlotElementToDTO(TimeSlotItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto(item.getActivity()), item.getTimeSlot());
    }

    private static CartElementDTO convertCartGroupElementToDTO(GroupItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto(item.getActivity()), CustomerCareController.convertGroupToDto(item.getGroup()));
    }

    private static CartElementDTO convertSkiPassElementToDTO(SkiPassItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto(item.getActivity()), item.getSkiPassType(), item.getDuration());
    }

}
