package teamb.w4e.interfaces.leisure;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ActivityFinder {
    @Query("SELECT a FROM Activity a WHERE a.name = :name AND a.booked = true")
    Optional<Activity> findActivityByName(@Param("name") String name);

    @Query("SELECT a FROM Activity a WHERE a.id = :id AND a.booked = true")
    Optional<Activity> findActivityById(Long id);

    @Query("SELECT a FROM Activity a WHERE a.id = :activityId AND a.booked = true")
    Activity retrieveActivity(@Param("activityId")Long activityId) throws IdNotFoundException;

    @Query("SELECT a FROM Activity a WHERE a.booked = true")
    List<Activity> findAllActivities();

}
