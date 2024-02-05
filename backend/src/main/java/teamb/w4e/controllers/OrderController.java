package teamb.w4e.controllers;

import teamb.w4e.dto.OrderDTO;
import teamb.w4e.entities.Order;
import teamb.w4e.exceptions.OrderIdNotFoundException;
import teamb.w4e.interfaces.OrderFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static teamb.w4e.controllers.OrderController.BASE_URI;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = BASE_URI, produces = APPLICATION_JSON_VALUE)
public class OrderController {

    public static final String BASE_URI = "/orders";

    private final OrderFinder orderFinder;

    @Autowired
    public OrderController(OrderFinder orderFinder) {
        this.orderFinder = orderFinder;
    }

    // Should be paginated
    @GetMapping
    public List<OrderDTO> listAllOrders() {
        return orderFinder.findAll().stream().map(OrderController::convertOrderToDto).toList();
    }

    @GetMapping("/{orderId}")
    public OrderDTO getAnOrder(@PathVariable Long orderId) throws OrderIdNotFoundException {
        return convertOrderToDto(orderFinder.retrieveOrder(orderId));
    }

    @GetMapping("/{orderId}/status")
    public String getAnOrderStatus(@PathVariable Long orderId) throws OrderIdNotFoundException {
        return orderFinder.retrieveOrderStatus(orderId).toString();
    }

    public static OrderDTO convertOrderToDto(Order order) { // In more complex cases, we could use a ModelMapper such as MapStruct
        return new OrderDTO(order.getId(), order.getCustomer().getId(), order.getPrice(), order.getPayReceiptId(), order.getStatus());
    }

}
