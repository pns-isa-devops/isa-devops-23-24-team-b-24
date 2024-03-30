package teamb.w4e.interfaces;

import teamb.w4e.entities.customers.Customer;

import java.util.Optional;

public interface Bank {

    Optional<String> pay(Customer customer, double value);
}
