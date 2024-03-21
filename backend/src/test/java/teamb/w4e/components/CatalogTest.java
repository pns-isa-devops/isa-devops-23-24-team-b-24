package teamb.w4e.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.AdvantageFinder;
import teamb.w4e.interfaces.AdvantageRegistration;
import teamb.w4e.interfaces.leisure.ActivityFinder;
import teamb.w4e.interfaces.leisure.ActivityRegistration;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CatalogTest {

    @Autowired
    private Catalog catalog;

    @Autowired
    private AdvantageRegistration advantageRegistration;

    @Autowired
    private ActivityRegistration activityRegistration;

    @Autowired
    private AdvantageFinder advantageFinder;

    @Autowired
    private ActivityFinder activityFinder;

    @Test
    void registerAdvantageSuccess() {
        Advantage advantage = catalog.register("name", AdvantageType.VIP, 10);
        Optional<Advantage> found = advantageFinder.findByName(advantage.getName());
        assertTrue(found.isPresent());
    }

    @Test
    void registerAdvantageFailure() {
        assertThrows(Exception.class, () -> catalog.register("name", AdvantageType.VIP, -10));
        assertThrows(Exception.class, () -> catalog.register("name", AdvantageType.VIP, 0));
        assertThrows(Exception.class, () -> catalog.register("name", null, 1001));
    }

    @Test
    void listAdvantageTypes() {
        assertNotNull(catalog.listAdvantageTypes());
        assertEquals(4, catalog.listAdvantageTypes().size());
    }

    @Test
    void registerActivityWithAdvantageSuccess() {
        Advantage advantage = advantageRegistration.register("name", AdvantageType.VIP, 10);
        Activity activity = activityRegistration.register("name", "description", 10.0, Set.of(advantage));
        Optional<Activity> found = activityFinder.findActivityByName(activity.getName());
        assertTrue(found.isPresent());
    }

    @Test
    void registerActivityWithAdvantageFailure() {
        Activity activity = activityRegistration.register("name", "description", 10.0, Collections.emptySet());
        Optional<Activity> found = activityFinder.findActivityByName(activity.getName());
        assertTrue(found.isPresent());
    }

    @Test
    void deleteActivity() throws IdNotFoundException {
        Activity activity = activityRegistration.register("name", "description", 10.0, Collections.emptySet());
        assertTrue(activityFinder.findActivityByName(activity.getName()).isPresent());
        activityRegistration.deleteActivity(activity.getId());
        Optional<Activity> found = activityFinder.findActivityByName(activity.getName());
        assertFalse(found.isPresent());
    }

    @Test
    void deleteService() throws IdNotFoundException {
        Activity activity = activityRegistration.register("name", "description", 10.0, Collections.emptySet());
        assertTrue(activityFinder.findActivityByName(activity.getName()).isPresent());
        activityRegistration.deleteActivity(activity.getId());
        Optional<Activity> found = activityFinder.findActivityByName(activity.getName());
        assertFalse(found.isPresent());
    }

}
