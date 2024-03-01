package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.Transaction;
import teamb.w4e.entities.cart.*;
import teamb.w4e.entities.reservations.*;
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
    public TimeSlotItem timeSlotUpdate(Long customerId, Activity activity, String date) throws NonValidDateForActivity, CustomerIdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getActivities();
        if (!scheduler.checkAvailability(activity, date)) {
            throw new NonValidDateForActivity(activity);
        }
        Optional<TimeSlotItem> existingItem = items.stream()
                .filter(item -> item.getTruc().equals(activity))
                .filter(item -> item.getType().equals(ReservationType.TIME_SLOT))
                .map(TimeSlotItem.class::cast).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setTimeSlot(date);
        } else {
            items.add(new TimeSlotItem(activity, date));
        }
        return new TimeSlotItem(activity, date);
    }

    @Override
    @Transactional
    public GroupItem groupUpdate(Long customerId, Activity activity, Group group) throws CustomerIdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getActivities();
        Optional<GroupItem> existingItem = items.stream()
                .filter(item -> item.getTruc().equals(activity))
                .filter(item -> item.getType().equals(ReservationType.GROUP))
                .map(GroupItem.class::cast).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setGroup(group);
        } else {
            items.add(new GroupItem(activity, group));
        }
        return new GroupItem(activity, group);
    }

    @Override
    public SkiPassItem skiPassUpdate(Long customerId, Activity activity, String type, int duration) throws CustomerIdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getActivities();
        Optional<SkiPassItem> existingItem = items.stream()
                .filter(item -> item.getTruc().equals(activity))
                .filter(item -> item.getType().equals(ReservationType.SKI_PASS))
                .map(SkiPassItem.class::cast).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setSkiPassType(type);
            existingItem.get().setDuration(duration);
        } else {
            items.add(new SkiPassItem(activity, type, duration));
        }
        return new SkiPassItem(activity, type, duration);
    }

    @Override
    public ServiceItem serviceUpdate(Long customerId, teamb.w4e.entities.catalog.Service service) throws CustomerIdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getActivities();
        Optional<ServiceItem> existingItem = items.stream()
                .filter(item -> item.getTruc().equals(service))
                .filter(item -> item.getType().equals(ReservationType.NONE))
                .map(ServiceItem.class::cast).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setTruc(service);
        } else {
            items.add(new ServiceItem(service));
        }
        return new ServiceItem(service);
    }

    @Override
    @Transactional
    public Set<Item> cartContent(Long customerId) throws CustomerIdNotFoundException {
        return customerFinder.retrieveCustomer(customerId).getCaddy().getActivities();
    }


    @Override
    @Transactional
    public Reservation validateActivity(Long customerId, Item item) throws EmptyCartException, PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if (customer.getCaddy().getActivities().isEmpty()) {
            throw new EmptyCartException(customer.getName());
        }
        if (item.getType().equals(ReservationType.TIME_SLOT)) {
            TimeSlotItem timeSlotItem = (TimeSlotItem) item;
            if (scheduler.reserve((Activity) timeSlotItem.getTruc(), timeSlotItem.getTimeSlot())) {
                TimeSlotReservation reservation = (TimeSlotReservation) payment.payReservationFromCart(customer, timeSlotItem);
                customer.getCaddy().getActivities().remove(item);
                return reservation;
            }
        }
        if (item.getType().equals(ReservationType.GROUP)) {
            GroupItem groupItem = (GroupItem) item;

            GroupReservation reservation = (GroupReservation) payment.payReservationFromCart(customer, groupItem);
            customer.getCaddy().getActivities().remove(item);
            return reservation;

        }
        if (item.getType().equals(ReservationType.SKI_PASS)) {
            SkiPassItem skiPassItem = (SkiPassItem) item;
            if (skiPass.reserve(customer.getName(), skiPassItem.getTruc().getName()).isPresent()) {
                SkiPassReservation reservation = (SkiPassReservation) payment.payReservationFromCart(customer, skiPassItem);
                customer.getCaddy().getActivities().remove(item);
                return reservation;
            }
        }
        throw new PaymentException(customer.getName(), item.getTruc().getPrice());
    }

    @Override
    @Transactional
    public Transaction validateService(Long customerId, ServiceItem item) throws EmptyCartException, PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if (customer.getCaddy().getActivities().isEmpty()) {
            throw new EmptyCartException(customer.getName());
        }
        Transaction transaction = payment.payServiceFromCart(customer, item);
        customer.getCaddy().getActivities().remove(item);
        return transaction;
    }
}
