package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.*;
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
    public Reservation createReservation(Customer customer, Item item, Transaction transaction) {
        return reservationRepository.save(new Reservation(item.getActivity(), item.getDate(), customer.getCard(), transaction));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findReservationByCard(Long cardId) {
        return reservationRepository.findReservationByCard(cardId);
    }
}
