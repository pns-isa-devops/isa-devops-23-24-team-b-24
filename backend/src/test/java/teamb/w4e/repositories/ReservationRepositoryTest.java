package teamb.w4e.repositories;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.reservations.*;
import teamb.w4e.entities.Transaction;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.reservation.ReservationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final Activity activity = new Activity("activity", "desc", 123, Set.of());
    private final Customer customer = new Customer("john", "1234567890");
    private final Transaction transaction = new Transaction(customer, 123, "1234567890");

    @BeforeEach
    void setUp() {
        customerRepository.saveAndFlush(customer);
        transactionRepository.saveAndFlush(transaction);
        activityCatalogRepository.saveAndFlush(activity);
    }

    @Test
    void testIdGenerationTimeSlotReservation() {
        Reservation reservation = new TimeSlotReservation(activity, "15-06 12:30", customer.getCard(), transaction);
        assertNull(reservation.getId());
        reservationRepository.saveAndFlush(reservation); // save in the persistent context and force saving in the DB (thus ensuring validation by Hibernate)
        assertNotNull(reservation.getId());
        assertNotNull(reservation.getTransaction());
    }

    @Test
    void testIdGenerationGroupReservation() {
        Customer jane = new Customer("jane", "1234567890");
        customerRepository.saveAndFlush(jane);
        Group group = new Group(customer, Set.of(jane));
        groupRepository.saveAndFlush(group);
        Reservation reservation = new GroupReservation(activity, group, customer.getCard(), transaction);
        assertNull(reservation.getId());
        reservationRepository.saveAndFlush(reservation); // save in the persistent context and force saving in the DB (thus ensuring validation by Hibernate)
        assertNotNull(reservation.getId());
        assertNotNull(reservation.getTransaction());
    }

    @Test
    void testIdGenerationSkiPassReservation() {
        Reservation reservation = new SkiPassReservation(activity, "day", 7, customer.getCard(), transaction);
        assertNull(reservation.getId());
        reservationRepository.saveAndFlush(reservation); // save in the persistent context and force saving in the DB (thus ensuring validation by Hibernate)
        assertNotNull(reservation.getId());
        assertNotNull(reservation.getTransaction());

    }

    @Test
    void testReservationWithoutTransaction() {
        Reservation reservation = new TimeSlotReservation(activity, "15-06 12:30", customer.getCard(), null);
        assertNull(reservation.getId());
        assertThrows(ConstraintViolationException.class, () -> reservationRepository.saveAndFlush(reservation));
    }

    @Test
    void testReservationWithoutRegisteredActivity() {
        Activity activity = new Activity("activity", "desc", 123, Set.of());
        Reservation reservation = new TimeSlotReservation(activity, "15-06 12:30", customer.getCard(), transaction);
        assertNull(reservation.getId());
        assertThrows(InvalidDataAccessApiUsageException.class, () -> reservationRepository.saveAndFlush(reservation));
    }


    @Test
    void testFindReservationByCard() {
        Reservation reservation = new TimeSlotReservation(activity, "15-06 12:30", customer.getCard(), transaction);
        reservationRepository.saveAndFlush(reservation);
        assertEquals(reservationRepository.findTimeSlotReservationByCard(customer.getCard().getId(), ReservationType.TIME_SLOT).get(0), reservation);
    }

    @Test
    void testDatePattern() {
        List<String> invalidDates = Arrays.asList(
                "john",
                "123456789",
                "01-00 00:00",
                "00-01 00:00",
                "32-12 23:59",
                "15-13 12:30",
                "15-06 24:30",
                "15-06 12:60",
                "15/06 12:30",
                "15-06-2022 12:30",
                "15-06 12:30 PM"
        );
        invalidDates.forEach(date -> {
            assertThrows(ConstraintViolationException.class, () -> {
                reservationRepository.saveAndFlush(new TimeSlotReservation(activity, date));
            });
        });
    }

    @Test
    void testSkiPassTypePattern() {
        List<String> invalidSkiPassTypes = Arrays.asList(
                "dayy",
                "half_dayy",
                "hourlyy",
                "day",
                "half_day",
                "hourly",
                "day ",
                " half_day",
                "hourly ",
                " ",
                "",
                "00"

        );
        invalidSkiPassTypes.forEach(skiPassType -> {
            assertThrows(ConstraintViolationException.class, () -> {
                reservationRepository.saveAndFlush(new SkiPassReservation(activity, skiPassType, 7));
            });
        });
    }

    @Test
    void testDurationPositive() {
        assertThrows(ConstraintViolationException.class, () -> {
            reservationRepository.saveAndFlush(new SkiPassReservation(activity, "day", 0));
            reservationRepository.saveAndFlush(new SkiPassReservation(activity, "day", -1));
        });
    }

    @Test
    void testFindReservationByCardAndType() {
        Reservation reservation = new TimeSlotReservation(activity, "15-06 12:30", customer.getCard(), transaction);
        reservationRepository.saveAndFlush(reservation);
        assertEquals(reservationRepository.findTimeSlotReservationByCard(customer.getCard().getId(), ReservationType.TIME_SLOT).get(0), reservation);
    }

    @Test
    void testNullGroup() {
        assertThrows(ConstraintViolationException.class, () -> {
            reservationRepository.saveAndFlush(new GroupReservation(activity, null, customer.getCard(), transaction));
        });
    }
}
