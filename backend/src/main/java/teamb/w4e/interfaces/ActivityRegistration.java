package teamb.w4e.interfaces;

import teamb.w4e.entities.Activity;
import teamb.w4e.entities.Advantage;

import java.util.Set;

public interface ActivityRegistration {
    Activity register(String name, String description, Set<Advantage> advantages);
}
