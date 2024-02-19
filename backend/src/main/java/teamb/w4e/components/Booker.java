package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Card;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
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
    public Reservation createReservation(Customer customer, Item item) {
        return reservationRepository.save(new Reservation(item.getActivity(), item.getDate(), customer.getCard()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findReservationByCard(Card card) {
        return reservationRepository.findReservationByCard(card);
    }
}
