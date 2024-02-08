package teamb.w4e.cucumber.ordering;

import teamb.w4e.entities.Customer;
import teamb.w4e.exceptions.AlreadyExistingCustomerException;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.EmptyCartException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.CartProcessor;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.CustomerRegistration;
import teamb.w4e.repositories.CustomerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@Transactional
public class BadOrdering {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private CustomerFinder customerFinder;

    @Autowired
    private CartProcessor cartProcessor;

    @Autowired // Spring/Cucumber bug workaround: autowired the mock declared in the Config class
    private Bank bankMock;

    private boolean validationRefused;

    @Before
    public void settingUpContext() throws PaymentException {
        customerRepository.deleteAll();
        when(bankMock.pay(any(Customer.class), anyDouble())).thenReturn(Optional.of("payReceiptIdOK"));
    }

    @Given("a bad customer")
    public void theBadCustomer() throws AlreadyExistingCustomerException {
        customerRegistration.register("BadBoy", "1234567890"); // give a consistent creditCard or get a validation exception for the ORM engine
    }

    @When("He validates an empty cart")
    public void validatesAnEmptyCart() throws CustomerIdNotFoundException {
        Customer badBoy = customerFinder.findByName("BadBoy").get();
        try {
            cartProcessor.validate(badBoy.getId());
        } catch (EmptyCartException e) {
            validationRefused = true;
            return;
        } catch (PaymentException e) {
        }
        validationRefused = false;
    }

    @Then("the order is not created")
    public void theOrderIsNotCreated() {
        assertTrue(validationRefused);
    }

}
