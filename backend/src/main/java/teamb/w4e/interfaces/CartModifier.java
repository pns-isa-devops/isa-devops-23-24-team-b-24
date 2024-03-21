package teamb.w4e.interfaces;

import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.cart.*;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NonValidDateForActivity;
import teamb.w4e.entities.catalog.Service;

import java.util.Set;

public interface CartModifier {

    TimeSlotItem timeSlotUpdate(Long customerId, Activity activity, String date) throws IdNotFoundException, NonValidDateForActivity, CustomerIdNotFoundException;

    GroupItem groupUpdate(Long customerId, Activity activity, Group group) throws IdNotFoundException, CustomerIdNotFoundException;

    SkiPassItem skiPassUpdate(Long customerId, Activity activity, String type, int duration) throws IdNotFoundException, CustomerIdNotFoundException;

    Set<Item> cartContent(Long customerId) throws IdNotFoundException, CustomerIdNotFoundException;

    ServiceItem serviceUpdate(Long customerId, Service service) throws CustomerIdNotFoundException;

    String removeItem(Long customerId, Long itemId) throws IdNotFoundException, CustomerIdNotFoundException;
}
