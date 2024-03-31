package teamb.w4e.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.reservations.GroupReservation;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.reservations.SkiPassReservation;
import teamb.w4e.entities.reservations.TimeSlotReservation;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.reservation.ReservationFinder;
import teamb.w4e.repositories.customers.GroupRepository;
import teamb.w4e.repositories.PartnerRepository;
import teamb.w4e.repositories.transactions.TransactionRepository;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.reservations.ReservationRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookerTest {

    @Autowired
    private CustomerRegistry customerRegistry;

    @Autowired
    private CustomerFinder customerFinder;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationFinder reservationFinder;
    private TimeSlotReservation timeSlotReservation;
    private GroupReservation groupReservation;
    private SkiPassReservation skiPassReservation;
    private Customer customer;


    @BeforeEach
    void setUp() throws AlreadyExistingException {
        customerRegistry.register("Hana", "1230896983");
        customer = customerFinder.findByName("Hana").orElse(null);
        partnerRepository.save(new Partner("partner"));
        Partner partner = partnerRepository.findPartnerByName("partner").orElse(null);
        Transaction transaction = new Transaction(customer, 100.0, "is ok");
        transactionRepository.save(transaction);

        Activity timeSlotActivity = new Activity(partner, "timeSlot", "tik tak", 100.0);
        activityCatalogRepository.save(timeSlotActivity);
        timeSlotReservation = new TimeSlotReservation(timeSlotActivity, "15-06 12:30", customer.getCard(), transaction);
        Activity skiActivity = new Activity(partner, "ski", "faire du ski", 100.0);
        activityCatalogRepository.save(skiActivity);
        skiPassReservation = new SkiPassReservation(skiActivity, "day", 5, customer.getCard(), transaction);

        customerRegistry.register("Noa", "1230896983");
        Customer anotherCustomer = customerFinder.findByName("Noa").orElse(customer);
        Group group = new Group(customer, Set.of(anotherCustomer));
        groupRepository.save(group);
        Activity groupActivity = new Activity(partner, "group", "union fait la force", 100.0);
        activityCatalogRepository.save(groupActivity);
        groupReservation = new GroupReservation(groupActivity, group, customer.getCard(), transaction);
    }

    @Test
    void testFindReservationById() {

        reservationRepository.save(timeSlotReservation);
        assertTrue(reservationRepository.findById(timeSlotReservation.getId()).isPresent());
        assertFalse(reservationRepository.findById(10L).isPresent());
    }

    @Test
    void testFindTimeSlotReservationByCard() {
        reservationRepository.save(timeSlotReservation);
        assertEquals(reservationFinder.findTimeSlotReservationByCard(customer.getCard().getId(), ReservationType.TIME_SLOT).get(0), timeSlotReservation);
        assertFalse(reservationRepository.findTimeSlotReservationByCard(15L, timeSlotReservation.getType()).contains(timeSlotReservation));

    }

    @Test
    void testFindGroupReservationByCard() {
        reservationRepository.save(groupReservation);
        assertTrue(reservationRepository.findGroupReservationByCard(customer.getId(), groupReservation.getType()).contains(groupReservation));
        assertFalse(reservationRepository.findGroupReservationByCard(-15L, groupReservation.getType()).contains(groupReservation));
    }

    @Test
    void testFindSkiPassReservationByCard() {
        reservationRepository.save(skiPassReservation);
        assertTrue(reservationRepository.findSkiPassReservationByCard(customer.getId(), skiPassReservation.getType()).contains(skiPassReservation));
        assertFalse(reservationRepository.findSkiPassReservationByCard(15L, skiPassReservation.getType()).contains(skiPassReservation));

    }

    @Test
    void testCreateGroupReservation() {
        reservationRepository.save(groupReservation);
        assertTrue(reservationRepository.findById(groupReservation.getId()).isPresent());
        assertFalse(reservationRepository.findById(10L).isPresent());
    }

    @Test
    void testCreateTimeSlotReservation() {
        reservationRepository.save(timeSlotReservation);
        assertTrue(reservationRepository.findById(timeSlotReservation.getId()).isPresent());
        assertFalse(reservationRepository.findById(10L).isPresent());
    }

    @Test
    void testCreateSkiPassReservation() {
        reservationRepository.save(skiPassReservation);
        assertTrue(reservationRepository.findById(skiPassReservation.getId()).isPresent());
        assertFalse(reservationRepository.findById(10L).isPresent());
    }
}
