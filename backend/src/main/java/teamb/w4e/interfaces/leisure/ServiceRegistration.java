package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.Service;

public interface ServiceRegistration {
    Service register(String name, String description, double price);
}
