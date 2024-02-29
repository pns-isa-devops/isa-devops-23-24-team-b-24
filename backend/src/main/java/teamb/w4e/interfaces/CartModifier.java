package teamb.w4e.interfaces;

import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.cart.GroupItem;
import teamb.w4e.entities.cart.Item;
import teamb.w4e.entities.cart.TimeSlotItem;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NonValidDateForActivity;

import java.util.Set;

public interface CartModifier {

    TimeSlotItem timeSlotUpdate(Long customerId, Activity activity, String date) throws IdNotFoundException, NonValidDateForActivity, CustomerIdNotFoundException;

    GroupItem groupUpdate(Long customerId, Activity activity, Group group) throws IdNotFoundException, CustomerIdNotFoundException;

    Set<Item> cartContent(Long customerId) throws IdNotFoundException, CustomerIdNotFoundException;
}
