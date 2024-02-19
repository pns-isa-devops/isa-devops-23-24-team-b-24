package teamb.w4e.interfaces.reservation;

import org.springframework.data.jpa.repository.Query;
import teamb.w4e.entities.Card;
import teamb.w4e.entities.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationFinder {
    Optional<Reservation> findReservationById(Long id);

    @Query("SELECT r FROM Reservation r WHERE r.card.id = :cardId")
    List<Reservation> findReservationByCard(Long cardId);

}
