package teamb.w4e.components;

import org.springframework.stereotype.Service;
import teamb.w4e.entities.Customer;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Payment;

@Service
public class TempCashier implements Payment {
    // This class is a placeholder for the payment system
    //TODO: Retrieve the payment system form Arnaud's branch

    @Override
    public boolean pay(Customer customer) throws PaymentException {
        return true;
    }
}
