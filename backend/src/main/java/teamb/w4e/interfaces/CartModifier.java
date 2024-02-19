package teamb.w4e.interfaces;

import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Item;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NonValidDateForActivity;

import java.util.Date;
import java.util.Set;

public interface CartModifier {

    Item update(Long customerId, Activity activity, Date date) throws IdNotFoundException, NonValidDateForActivity;

    Set<Item> cartContent(Long customerId) throws IdNotFoundException;
}
