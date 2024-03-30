package teamb.w4e.interfaces;

import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.transactions.Transaction;

public interface TransactionCreator {

    Transaction createTransaction(Customer customer, double amount, String paymentId);
}
