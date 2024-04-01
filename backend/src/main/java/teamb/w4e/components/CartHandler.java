package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.items.*;
import teamb.w4e.entities.reservations.*;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.exceptions.*;
import teamb.w4e.interfaces.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public TimeSlotItem timeSlotUpdate(Long customerId, Activity activity, String date) throws NonValidDateForActivity, IdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getCatalogItem();
        if (!scheduler.checkAvailability(activity, date)) {
            throw new NonValidDateForActivity(activity);
        }
        Optional<TimeSlotItem> existingItem = items.stream()
                .filter(item -> item.getType().equals(ReservationType.TIME_SLOT) && ((TimeSlotItem) item).getActivity().equals(activity))
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
    public GroupItem groupUpdate(Long customerId, Activity activity, Group group) throws IdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getCatalogItem();
        Optional<GroupItem> existingItem = items.stream()
                .filter(item -> item.getType().equals(ReservationType.GROUP) && ((GroupItem) item).getActivity().equals(activity))
                .map(GroupItem.class::cast).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setGroup(group);
        } else {
            items.add(new GroupItem(activity, group));
        }
        return new GroupItem(activity, group);
    }

    @Override
    @Transactional
    public SkiPassItem skiPassUpdate(Long customerId, Activity activity, String type, int duration) throws IdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getCatalogItem();
        Optional<SkiPassItem> existingItem = items.stream()
                .filter(item -> item.getType().equals(ReservationType.SKI_PASS) && ((SkiPassItem) item).getActivity().equals(activity))
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
    @Transactional
    public ServiceItem serviceUpdate(Long customerId, teamb.w4e.entities.catalog.Service service) throws IdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Set<Item> items = customer.getCaddy().getCatalogItem();
        items.add(new ServiceItem(service));
        return new ServiceItem(service);
    }

    @Override
    @Transactional
    public AdvantageItem advantageUpdate(Long customerId, Advantage advantage) throws IdNotFoundException, AlreadyExistingException, NegativeAmountTransactionException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if (customer.getCard().getPoints() < advantage.getPoints()) {
            throw new NegativeAmountTransactionException(advantage.getPoints());
        }
        Set<Item> items = customer.getCaddy().getCatalogItem();
        Optional<AdvantageItem> existingItem = items.stream()
                .filter(item -> item.getType().equals(ReservationType.NONE) && item.getName().equals(advantage.getName()))
                .map(AdvantageItem.class::cast)
                .filter(item -> item.getAdvantage().equals(advantage))
                .findFirst();
        if (existingItem.isPresent()) {
            throw new AlreadyExistingException(advantage.getName());
        }
        items.add(new AdvantageItem(advantage));
        return new AdvantageItem(advantage);
    }

    @Override
    @Transactional
    public Set<AdvantageItem> advantageCartContent(Long customerId) throws IdNotFoundException {
        return customerFinder.retrieveCustomer(customerId).getCaddy().getCatalogItem().stream()
                .filter(item -> item.getType().equals(ReservationType.NONE))
                .map(AdvantageItem.class::cast)
                .collect(Collectors.toSet());
    }


    @Override
    @Transactional
    public Set<Item> cartContent(Long customerId) throws IdNotFoundException {
        return customerFinder.retrieveCustomer(customerId).getCaddy().getCatalogItem().stream()
                .filter(item -> !item.getType().equals(ReservationType.NONE))
                .collect(Collectors.toSet());
    }


    @Override
    @Transactional
    public Reservation validateActivity(Long customerId, Item item) throws EmptyCartException, PaymentException, NegativeAmountTransactionException, IdNotFoundException, CannotReserveException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if (customer.getCaddy().getCatalogItem().isEmpty()) {
            throw new EmptyCartException(customer.getName());
        }
        if (item.getType().equals(ReservationType.TIME_SLOT)) {
            TimeSlotItem timeSlotItem = (TimeSlotItem) item;
            if (scheduler.reserve(timeSlotItem.getActivity(), timeSlotItem.getTimeSlot())) {
                TimeSlotReservation reservation = (TimeSlotReservation) payment.payReservationFromCart(customer, timeSlotItem);
                customer.getCaddy().getCatalogItem().remove(item);
                return reservation;
            }
        }
        if (item.getType().equals(ReservationType.GROUP)) {
            GroupItem groupItem = (GroupItem) item;
            GroupReservation reservation = (GroupReservation) payment.payReservationFromCart(customer, groupItem);
            customer.getCaddy().getCatalogItem().remove(item);
            return reservation;

        }
        if (item.getType().equals(ReservationType.SKI_PASS)) {
            SkiPassItem skiPassItem = (SkiPassItem) item;
            if (skiPass.reserve(skiPassItem.getActivity().getName(), skiPassItem.getSkiPassType(), skiPassItem.getDuration()).isPresent()) {
                SkiPassReservation reservation = (SkiPassReservation) payment.payReservationFromCart(customer, skiPassItem);
                customer.getCaddy().getCatalogItem().remove(item);
                return reservation;
            }
        }
        throw new CannotReserveException(item.getType().getType());
    }

    @Override
    @Transactional
    public Transaction validateService(Long customerId, ServiceItem item) throws EmptyCartException, PaymentException, NegativeAmountTransactionException, IdNotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if (customer.getCaddy().getCatalogItem().isEmpty()) {
            throw new EmptyCartException(customer.getName());
        }
        Transaction transaction = payment.payServiceFromCart(customer, item);
        customer.getCaddy().getCatalogItem().remove(item);
        return transaction;
    }
}
