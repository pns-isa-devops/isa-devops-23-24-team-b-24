package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.Service;

public interface ServiceRegistration {
    Service registerService(String name, String description, double price);
}
