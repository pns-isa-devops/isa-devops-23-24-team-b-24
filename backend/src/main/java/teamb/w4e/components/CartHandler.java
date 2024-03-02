package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.*;

import java.util.Optional;
import java.util.Set;

@Service
public class CartHandler implements CartProcessor, CartModifier {

    private final CustomerFinder customerFinder;

    private final Payment payment;

    private final Scheduler scheduler;

    private final SkiPass skiPass;

    @Autowired
    public CartHandler(CustomerFinder customerFinder, Payment payment, Scheduler scheduler, SkiPass skiPass) {
        this.customerFinder = customerFinder;
        this.payment = payment;
        this.scheduler = scheduler;
        this.skiPass = skiPass;
    }

    @Override
    @Transactional
    public Item update(Long customerId, Activity activity, String date) throws NonValidDateForActivity, CustomerIdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getActivities();
        if (!scheduler.checkAvailability(activity, date)) {
            throw new NonValidDateForActivity(activity);
        }
        Optional<Item> existingItem = items.stream().filter(it -> it.getActivity().equals(activity)).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setDate(date);
        } else {
            items.add(new Item(activity, date));
        }

        return new Item(activity, date);
    }

    @Override
    @Transactional
    public Set<Item> cartContent(Long customerId) throws CustomerIdNotFoundException {
        return customerFinder.retrieveCustomer(customerId).getCaddy().getActivities();
    }

    @Override
    @Transactional
    public Reservation validate(Long customerId, Item item, ReservationType type) throws EmptyCartException, PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if (customer.getCaddy().getActivities().isEmpty()) {
            throw new EmptyCartException(customer.getName());
        }
        Reservation reservation = payment.payReservationFromCart(customer, item, type);
        if (reservation.getType() == ReservationType.SKI_PASS) {
            skiPass.reserve(customer, item.getActivity());
        }
        customer.getCaddy().getActivities().remove(item);
        return reservation;
    }
}
