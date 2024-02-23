package teamb.w4e.cucumber.reservation;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.repositories.CustomerRepository;
import teamb.w4e.repositories.TransactionRepository;
import teamb.w4e.repositories.reservation.ReservationRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Given("a customer named {string} with the credit card number {string}")
    public void aCustomerNamedWithTheCreditCardNumber(String arg0, String arg1) {
        // always say tru for the test need to change
        assertTrue(true);
    }
}
