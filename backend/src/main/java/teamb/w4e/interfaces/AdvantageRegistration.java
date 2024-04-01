package teamb.w4e.interfaces;

import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.exceptions.NotFoundException;

public interface AdvantageRegistration {

    Advantage register(String partnerName, String name, AdvantageType type, int points) throws NotFoundException;
}
