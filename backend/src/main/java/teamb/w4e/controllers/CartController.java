package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.TransactionDTO;
import teamb.w4e.dto.cart.CartElementDTO;
import teamb.w4e.dto.reservations.ReservationDTO;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.ServiceItem;
import teamb.w4e.entities.cart.SkiPassItem;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.CartModifier;
import teamb.w4e.interfaces.CartProcessor;
import teamb.w4e.interfaces.GroupFinder;
import teamb.w4e.interfaces.leisure.ActivityFinder;
import teamb.w4e.interfaces.leisure.ServiceFinder;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CartController {

    public static final String CART_URI = "/{customerId}/cart";

    private final CartProcessor processor;
    private final CartModifier cart;

    private final ActivityFinder activityFinder;
    private final ServiceFinder serviceFinder;
    private final GroupFinder groupFinder;


    @Autowired
    public CartController(CartProcessor processor, CartModifier cart, ActivityFinder activityFinder, ServiceFinder serviceFinder, GroupFinder groupFinder) {
        this.processor = processor;
        this.cart = cart;
        this.activityFinder = activityFinder;
        this.serviceFinder = serviceFinder;
        this.groupFinder = groupFinder;
    }

    @PostMapping(path = CART_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CartElementDTO> updateCustomerCart(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartDTO) throws IdNotFoundException, NonValidDateForActivity {
        if (cartDTO.getType().equals(ReservationType.NONE)) {
            Service service = serviceFinder.retrieveService(cartDTO.getLeisure().id());
            return ResponseEntity.ok(convertServiceElementToDTO(cart.serviceUpdate(customerId, service)));
        } else if (cartDTO.getType().equals(ReservationType.TIME_SLOT)) {
            Activity activity = activityFinder.retrieveActivity(cartDTO.getLeisure().id());
            return ResponseEntity.ok(convertTimeSlotElementToDTO(cart.timeSlotUpdate(customerId, activity, cartDTO.getDate())));
        } else if (cartDTO.getType().equals(ReservationType.GROUP)) {
            Activity activity = activityFinder.retrieveActivity(cartDTO.getLeisure().id());
            return ResponseEntity.ok(convertCartGroupElementToDTO(cart.groupUpdate(customerId, activity, groupFinder.retrieveGroup(customerId))));
        } else {
            Activity activity = activityFinder.retrieveActivity(cartDTO.getLeisure().id());
            return ResponseEntity.ok(convertSkiPassElementToDTO(cart.skiPassUpdate(customerId, activity, cartDTO.getSkiPassType(), cartDTO.getDuration())));
        }
    }

    @GetMapping(path = CART_URI)
    public ResponseEntity<Set<CartElementDTO>> getCustomerCartContents(@PathVariable("customerId") Long customerId) throws IdNotFoundException {
        Set<CartElementDTO> cartElements = cart.cartContent(customerId).stream().map(item -> {
            ReservationType type = item.getType();
            return switch (type) {
                case TIME_SLOT -> convertTimeSlotElementToDTO((TimeSlotItem) item);
                case GROUP -> convertCartGroupElementToDTO((GroupItem) item);
                case SKI_PASS -> convertSkiPassElementToDTO((SkiPassItem) item);
                case NONE -> convertServiceElementToDTO((ServiceItem) item);
            };
        }).collect(Collectors.toSet());
        return ResponseEntity.ok(cartElements);
    }

    @PostMapping(path = CART_URI + "/reservation", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> reserve(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartElementDTO) throws EmptyCartException, PaymentException, IdNotFoundException, NegativeAmountTransactionException {

        ReservationType type = cartElementDTO.getType();
        return switch (type) {
            case TIME_SLOT ->
                    ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validateActivity(customerId, new TimeSlotItem(activityFinder.retrieveActivity(cartElementDTO.getLeisure().id()), cartElementDTO.getDate()))));
            case GROUP ->
                    ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validateActivity(customerId, new GroupItem(activityFinder.retrieveActivity(cartElementDTO.getLeisure().id()), groupFinder.retrieveGroup(customerId)))));
            case SKI_PASS ->
                    ResponseEntity.ok().body(ReservationController.convertReservationToDTO(processor.validateActivity(customerId, new SkiPassItem(activityFinder.retrieveActivity(cartElementDTO.getLeisure().id()), cartElementDTO.getSkiPassType(), cartElementDTO.getDuration()))));
            case NONE -> ResponseEntity.noContent().build();
        };
    }

    @PostMapping(path = CART_URI + "/use-service", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> serviceUtilisation(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartElementDTO cartElementDTO) throws EmptyCartException, PaymentException, IdNotFoundException, NegativeAmountTransactionException {
        if (cartElementDTO.getType().equals(ReservationType.NONE)) {
            ServiceItem serviceItem = new ServiceItem(serviceFinder.retrieveService(cartElementDTO.getLeisure().id()));
            return ResponseEntity.ok().body(TransactionController.convertTransactionToDto(processor.validateService(customerId, serviceItem)));
        }
        // on renvoit un 204 ? Oui je pense
        return ResponseEntity.noContent().build();
        // on dit que ça n'est bien passé mais qu'il y a rien
    }


    private static CartElementDTO convertTimeSlotElementToDTO(TimeSlotItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto((Activity) item.getLeisure()), item.getTimeSlot());
    }

    private static CartElementDTO convertCartGroupElementToDTO(GroupItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto((Activity) item.getLeisure()), CustomerCareController.convertGroupToDto(item.getGroup()));
    }

    private static CartElementDTO convertSkiPassElementToDTO(SkiPassItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertActivityToDto((Activity) item.getLeisure()), item.getSkiPassType(), item.getDuration());
    }

    private static CartElementDTO convertServiceElementToDTO(ServiceItem item) {
        return new CartElementDTO(item.getType(), LeisureController.convertServiceToDto((Service) item.getLeisure()));
    }
}
