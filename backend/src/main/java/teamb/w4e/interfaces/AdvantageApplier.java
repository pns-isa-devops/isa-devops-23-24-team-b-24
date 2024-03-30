package teamb.w4e.interfaces;

import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.Leisure;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;

public interface AdvantageApplier {

    PointTransaction apply(Customer customer, Advantage advantage, Leisure leisure) throws NegativeAmountTransactionException;

    PointTransaction reduction(Customer customer, Advantage advantage, Leisure leisure) throws NegativeAmountTransactionException, IdNotFoundException;


}
