package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.PointTransaction;
import teamb.w4e.entities.Transaction;

public interface TransactionCreator {

    Transaction createTransaction(Customer customer, double amount, String paymentId);

    PointTransaction createPointTransaction(Customer customer, int amount, Partner issuer);
}
