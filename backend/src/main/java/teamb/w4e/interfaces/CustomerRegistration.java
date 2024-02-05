package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.exceptions.AlreadyExistingCustomerException;

public interface CustomerRegistration {

    Customer register(String name, String creditCard)
            throws AlreadyExistingCustomerException;
}
