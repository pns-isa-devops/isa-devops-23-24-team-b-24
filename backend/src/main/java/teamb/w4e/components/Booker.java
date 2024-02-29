package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.*;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.entities.reservations.GroupReservation;
import teamb.w4e.entities.reservations.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.reservations.TimeSlotReservation;
import teamb.w4e.interfaces.reservation.ReservationCreator;
import teamb.w4e.interfaces.reservation.ReservationFinder;
import teamb.w4e.repositories.reservation.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class Booker implements ReservationCreator, ReservationFinder {

    private final ReservationRepository reservationRepository;

    @Autowired
    public Booker(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<TimeSlotReservation> findTimeSlotReservationByCard(Long cardId, ReservationType type) {
        return reservationRepository.findTimeSlotReservationByCard(cardId, type);
    }

    @Override
    public List<GroupReservation> findGroupReservationByCard(Long cardId, ReservationType type) {
        return reservationRepository.findGroupReservationByCard(cardId, type);
    }


    @Override
    public GroupReservation createGroupReservation(Customer customer, GroupItem item, Transaction transaction) {
        return reservationRepository.save(new GroupReservation(item.getActivity(), item.getGroup(), customer.getCard(), transaction));
    }

    @Override
    public TimeSlotReservation createTimeSlotReservation(Customer customer,TimeSlotItem item, Transaction transaction) {
        return reservationRepository.save(new TimeSlotReservation(item.getActivity(), item.getTimeSlot(), customer.getCard(), transaction));
    }
}
