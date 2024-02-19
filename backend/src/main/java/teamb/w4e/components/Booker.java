package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
import teamb.w4e.interfaces.reservation.ReservationCreator;
import teamb.w4e.repositories.reservation.ReservationRepository;

@Service
public class Booker implements ReservationCreator {

    private final ReservationRepository reservationRepository;

    @Autowired
    public Booker(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation createReservation(Customer customer, Item item) {
        return reservationRepository.save(new Reservation(item.getActivity(), item.getDate(), customer.getCard()));
    }
}
