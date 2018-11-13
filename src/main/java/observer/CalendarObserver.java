package observer;

import com.google.api.services.calendar.Calendar;

public interface CalendarObserver {
    void update(Calendar calendar, long timeToSet);
}
