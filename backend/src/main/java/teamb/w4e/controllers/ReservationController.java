package teamb.w4e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamb.w4e.dto.ReservationDTO;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Reservation;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.reservation.ReservationFinder;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ReservationController {

    private static final String RESERVATION_URI = "/{customerId}/reservations";

    private final ReservationFinder reservationFinder;

    private final CustomerFinder customerFinder;

    @Autowired
    public ReservationController(ReservationFinder reservationFinder, CustomerFinder customerFinder) {
        this.reservationFinder = reservationFinder;
        this.customerFinder = customerFinder;
    }

    @RequestMapping(path = RESERVATION_URI)
    public ResponseEntity<List<ReservationDTO>> getCustomerReservations(@PathVariable("customerId") Long customerId) throws IdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        return ResponseEntity.ok(reservationFinder.findReservationByCard(customer.getCard()).stream().map(ReservationController::convertReservationToDTO).toList());
    }


    public static ReservationDTO convertReservationToDTO(Reservation reservation) {
        return new ReservationDTO(reservation.getId(), LeisureController.convertActivityToDto(reservation.getActivity()), reservation.getDate());
    }
}

