package observer;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

import java.util.List;

public interface CalendarObserver {
    void update(List<Event> events);
}
