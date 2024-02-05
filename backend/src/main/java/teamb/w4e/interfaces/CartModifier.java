package teamb.w4e.interfaces;

import teamb.w4e.entities.Item;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.NegativeQuantityException;

import java.util.Set;

public interface CartModifier {

    Item update(Long customerId, Item it) throws NegativeQuantityException, CustomerIdNotFoundException;

    Set<Item> cartContent(Long customerId) throws CustomerIdNotFoundException;

}
