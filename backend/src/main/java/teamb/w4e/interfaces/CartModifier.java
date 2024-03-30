package teamb.w4e.interfaces;

import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.items.*;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.NonValidDateForActivity;
import teamb.w4e.entities.catalog.Service;

import java.util.Set;

public interface CartModifier {

    TimeSlotItem timeSlotUpdate(Long customerId, Activity activity, String date) throws NonValidDateForActivity, IdNotFoundException;

    GroupItem groupUpdate(Long customerId, Activity activity, Group group) throws IdNotFoundException;

    SkiPassItem skiPassUpdate(Long customerId, Activity activity, String type, int duration) throws IdNotFoundException;

    Set<Item> cartContent(Long customerId) throws IdNotFoundException;

    ServiceItem serviceUpdate(Long customerId, Service service) throws IdNotFoundException;

    Advantage advantageUpdate(Long customerId, Advantage advantage) throws IdNotFoundException, AlreadyExistingException, NegativeAmountTransactionException;

    Set<Advantage> advantageCartContent(Long customerId) throws IdNotFoundException;
}
