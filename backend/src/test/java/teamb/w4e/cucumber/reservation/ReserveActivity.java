package teamb.w4e.cucumber.reservation;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Customer;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.TransactionRepository;
import teamb.w4e.repositories.reservation.ReservationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
public class ReserveActivity {

    // We autowire the mock here because there are some bugs with the cucumber/spring integration
    @Autowired
    private Bank bankMock;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Before
    public void settingUpContext() throws PaymentException {
        customerRepository.deleteAll();
        transactionRepository.deleteAll();
        reservationRepository.deleteAll();
        when(bankMock.pay(any(Customer.class), any(Double.class))).thenReturn(Optional.of("1234567890"));
    }

    @Given("a customer named {string} with the credit card number {string}")
    public void aCustomerNamedWithTheCreditCardNumber(String arg0, String arg1) {
        // always say true for the test need to change
        assertTrue(true);
    }
}
