package teamb.w4e.repositories.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamb.w4e.entities.reservations.GroupReservation;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.reservations.TimeSlotReservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM TimeSlotReservation r WHERE r.card.id = :cardId AND r.type = :type")
    List<TimeSlotReservation> findTimeSlotReservationByCard(@Param("cardId") Long cartId, @Param("type")ReservationType type);

    @Query("SELECT r FROM GroupReservation r WHERE r.card.id = :cardId AND r.type = :type")
    List<GroupReservation> findGroupReservationByCard(@Param("cardId") Long cardId, @Param("type")ReservationType type);

}
