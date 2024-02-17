package teamb.w4e.interfaces;

import teamb.w4e.entities.Activity;
import teamb.w4e.entities.AdvantageType;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ActivityFinder {
    Optional<Activity> findActivityById(Long id);

    Activity retrieveActivity(Long activityId) throws IdNotFoundException;
    List<Activity> findAllActivities();
}
