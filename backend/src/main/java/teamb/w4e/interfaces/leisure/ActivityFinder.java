package teamb.w4e.interfaces.leisure;

import teamb.w4e.entities.Activity;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ActivityFinder {
Optional<Activity> findActivityByName(String name);
    Optional<Activity> findActivityById(Long id);
    Activity retrieveActivity(Long activityId) throws IdNotFoundException;
    List<Activity> findAllActivities();
}
