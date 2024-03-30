package teamb.w4e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamb.w4e.dto.reservations.ReservationDTO;
import teamb.w4e.entities.reservations.*;
import teamb.w4e.interfaces.reservation.ReservationFinder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = CustomerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ReservationController {

    private static final String RESERVATION_URI = "/{customerId}/reservations";

    private final ReservationFinder reservationFinder;


    @Autowired
    public ReservationController(ReservationFinder reservationFinder) {
        this.reservationFinder = reservationFinder;
    }

    @RequestMapping(path = RESERVATION_URI)
    public ResponseEntity<Set<ReservationDTO>> getCustomerReservations(@PathVariable("customerId") Long customerId) {
        Set<TimeSlotReservation> timeSlotReservations = new HashSet<>(reservationFinder.findTimeSlotReservationByCard(customerId, ReservationType.TIME_SLOT));
        Set<GroupReservation> groupReservations = new HashSet<>(reservationFinder.findGroupReservationByCard(customerId, ReservationType.GROUP));
        Set<SkiPassReservation> skiPassReservations = new HashSet<>(reservationFinder.findSkiPassReservationByCard(customerId, ReservationType.SKI_PASS));
        Set<ReservationDTO> reservations = new HashSet<>();
        reservations.addAll(timeSlotReservations.stream().map(ReservationController::convertTimeSlotReservationToDTO).collect(Collectors.toSet()));
        reservations.addAll(groupReservations.stream().map(ReservationController::convertGroupReservationToDTO).collect(Collectors.toSet()));
        reservations.addAll(skiPassReservations.stream().map(ReservationController::convertSkiPassReservationToDTO).collect(Collectors.toSet()));
        return ResponseEntity.ok(reservations);
    }

    public static ReservationDTO convertReservationToDTO(Reservation reservation) {
        ReservationType type = reservation.getType();
        if (type.equals(ReservationType.TIME_SLOT)) {
            return convertTimeSlotReservationToDTO((TimeSlotReservation) reservation);
        } else if (type.equals(ReservationType.GROUP)) {
            return convertGroupReservationToDTO((GroupReservation) reservation);
        }
        return convertSkiPassReservationToDTO((SkiPassReservation) reservation);
    }

    private static ReservationDTO convertTimeSlotReservationToDTO(TimeSlotReservation reservation) {
        return new ReservationDTO(reservation.getId(), reservation.getType(), LeisureController.convertActivityToDto(reservation.getActivity()), reservation.getTimeSlot());
    }

    private static ReservationDTO convertGroupReservationToDTO(GroupReservation reservation) {
        return new ReservationDTO(reservation.getId(), reservation.getType(), LeisureController.convertActivityToDto(reservation.getActivity()), GroupController.convertGroupToDto(reservation.getGroup()));
    }

    private static ReservationDTO convertSkiPassReservationToDTO(SkiPassReservation reservation) {
        return new ReservationDTO(reservation.getId(), reservation.getType(), LeisureController.convertActivityToDto(reservation.getActivity()), reservation.getSkiPassType(), reservation.getDuration());
    }


}

