package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.PaymentException;

public interface Payment {

    Transaction createTransaction(Customer customer, double amount) throws PaymentException, NegativeAmountTransactionException;

}
