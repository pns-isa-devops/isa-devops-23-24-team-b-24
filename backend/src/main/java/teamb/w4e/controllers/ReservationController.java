package teamb.w4e.controllers;

import teamb.w4e.dto.ReservationDTO;
import teamb.w4e.entities.Reservation;


public class ReservationController {



    public static ReservationDTO convertReservationToDTO(Reservation reservation) {
        return new ReservationDTO(reservation.getId(), LeisureController.convertActivityToDto(reservation.getActivity()), reservation.getDate());
    }
}

