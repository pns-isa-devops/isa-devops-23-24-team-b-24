package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Item;
import teamb.w4e.entities.Reservation;
import teamb.w4e.exceptions.EmptyCartException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NonValidDateForActivity;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartHandler implements CartProcessor, CartModifier {

    private final CustomerFinder customerFinder;

    private final Payment payment;

    private final Scheduler scheduler;

    @Autowired
    public CartHandler(CustomerFinder customerFinder, Payment payment, Scheduler scheduler) {
        this.customerFinder = customerFinder;
        this.payment = payment;
        this.scheduler = scheduler;
    }

    @Override
    @Transactional
    public Item update(Long customerId, Activity activity, Date date) throws IdNotFoundException, NonValidDateForActivity {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getActivities();
        if (!scheduler.checkAvailability(activity, date)) {
            throw new NonValidDateForActivity(activity);
        }
        Optional<Item> existingItem = items.stream().filter(it -> it.getActivity().equals(activity)).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setReservationDate(date);
        } else {
            items.add(new Item(activity, date));
        }

        return new Item(activity, date);
    }

    @Override
    @Transactional
    public Set<Item> cartContent(Long customerId) throws IdNotFoundException {
        return customerFinder.retrieveCustomer(customerId).getCaddy().getActivities();
    }

    @Override
    @Transactional
    public Set<Reservation> validate(Long customerId) throws IdNotFoundException, EmptyCartException, PaymentException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if (customer.getCaddy().getActivities().isEmpty()) {
            throw new EmptyCartException(customer.getName());
        }
        if (!payment.pay(customer)) {
            throw new PaymentException(customer.getName(),0);
        }
        Set<Reservation> reservations = customer.getCaddy().getActivities().stream()
                .map(item -> scheduler.reserve(item.getActivity(), item.getReservationDate()))
                .collect(Collectors.toSet());
        customer.getCaddy().getActivities().clear();
        return reservations;
    }
}
