package observer;

import com.google.api.services.calendar.Calendar;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<CalendarObserver> calendarObservers = new ArrayList<>();

    public void addObserver(CalendarObserver calendarObserver) {
        if(!calendarObservers.contains(calendarObserver)) {
            calendarObservers.add(calendarObserver);
        }
     }

     public void notifyObservers(Calendar calendar, long timeToSet) {
        for (CalendarObserver calendarObserver : calendarObservers) {
            calendarObserver.update(calendar, timeToSet);
        }
     }

}
