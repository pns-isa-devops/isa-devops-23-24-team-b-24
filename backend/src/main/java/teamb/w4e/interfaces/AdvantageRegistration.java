package teamb.w4e.interfaces;

import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;

public interface AdvantageRegistration {

    Advantage register(String name, AdvantageType type, int points);
}
