package teamb.w4e.interfaces;

import teamb.w4e.entities.Partner;
import teamb.w4e.exceptions.AlreadyExistingException;

public interface PartnerRegistration {

    Partner register(String name) throws AlreadyExistingException;
}
