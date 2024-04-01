package teamb.w4e.interfaces;

import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.Leisure;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.NotFoundException;

public interface AdvantageApplier {

    PointTransaction apply(Customer customer, Advantage advantage, Item item) throws NegativeAmountTransactionException, AlreadyExistingException, IdNotFoundException;

    PointTransaction reduction(Customer customer, Advantage advantage, Item item) throws NegativeAmountTransactionException, IdNotFoundException, NotFoundException;


}
