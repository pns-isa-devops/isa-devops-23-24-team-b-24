package teamb.w4e.interfaces.reservation;

import org.springframework.data.jpa.repository.Query;
import teamb.w4e.entities.reservations.GroupReservation;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.reservations.TimeSlotReservation;

import java.util.List;
import java.util.Optional;

public interface ReservationFinder {
    Optional<Reservation> findReservationById(Long id);

    @Query("SELECT r FROM TimeSlotReservation r WHERE r.card.id = :cardId AND r.type = :type")
    List<TimeSlotReservation> findTimeSlotReservationByCard(Long cardId, ReservationType type);
    @Query("SELECT r FROM GroupReservation r WHERE r.card.id = :cardId AND r.type = :type")
    List<GroupReservation> findGroupReservationByCard(Long cardId, ReservationType type);




}
