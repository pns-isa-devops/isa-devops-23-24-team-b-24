package teamb.w4e.cucumber.reservation;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.Scheduler;

@CucumberContextConfiguration
@SpringBootTest
public class ReservationCucumberConfig {

    // we need to mock the service here because there are some bugs with the cucumber/spring integration
    // we autowired and set up the mock in the step definitions. It's normal.
    @MockBean
    private Bank bankMock;

    @MockBean
    private Scheduler schedulerMock;
}
