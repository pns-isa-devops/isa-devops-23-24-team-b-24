package teamb.w4e.interfaces;

import teamb.w4e.entities.Advantage;
import teamb.w4e.entities.AdvantageType;

public interface AdvantageRegistration {

    Advantage register(String name, AdvantageType type, int points);
}
