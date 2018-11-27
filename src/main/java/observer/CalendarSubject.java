package observer;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;
import java.util.List;

public class CalendarSubject {
    private List<CalendarObserver> calendarObservers = new ArrayList<>();

    public void addObserver(CalendarObserver calendarObserver) {
        if(!calendarObservers.contains(calendarObserver)) {
            calendarObservers.add(calendarObserver);
        }
     }

     public void notifyObservers(List<Event> addedEvents) {
        for (CalendarObserver calendarObserver : calendarObservers) {
            calendarObserver.update(addedEvents);
        }
     }

}
