package teamb.w4e.interfaces.reservation;

import teamb.w4e.entities.Card;
import teamb.w4e.entities.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationFinder {
    Optional<Reservation> findReservationById(Long id);

    List<Reservation> findReservationByCard(Card card);

}
