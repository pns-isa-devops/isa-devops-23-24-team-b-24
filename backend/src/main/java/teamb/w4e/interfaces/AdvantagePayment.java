package teamb.w4e.interfaces;

import teamb.w4e.entities.Partner;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.exceptions.NegativeAmountTransactionException;

public interface AdvantagePayment {
    PointTransaction payAdvantageFromCart(Customer customer, Advantage advantage, Partner partner) throws NegativeAmountTransactionException;
}
