package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CustomerFinder {

    Optional<Customer> findByName(String name);

    Optional<Customer> findById(Long id);

    Customer retrieveCustomer(Long customerId) throws IdNotFoundException;

    List<Customer> findAll();

}
