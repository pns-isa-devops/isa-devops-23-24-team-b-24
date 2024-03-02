package teamb.w4e.interfaces;

import java.util.Optional;

import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Customer;

public interface SkiPass {
    Optional<String> reserve(Customer customer, Activity activity);
}
