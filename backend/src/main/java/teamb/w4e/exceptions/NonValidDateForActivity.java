package teamb.w4e.exceptions;

import teamb.w4e.entities.catalog.Activity;

public class NonValidDateForActivity extends Exception {

    public NonValidDateForActivity(Activity activity) {
        super("The date is not valid for the activity " + activity.getName());
    }
}
