package ui;

import Model.TodoListCalendar;
import Model.TodoListEntryActivity;
import Model.TodoListEntry;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import observer.CalendarObserver;

import java.io.IOException;
import java.util.*;

public class ListPrinter implements CalendarObserver {
    // EFFECTS: Prints out TodoList file names
    public void printTodoListFileNames(ArrayList<String> listTodoListNames) {
        for (String todoListName : listTodoListNames) {
            System.out.println("File name: " + todoListName);
        }
    }

    public void printMap(Map<TodoListEntryActivity, TodoListEntry> todoListEntryHashMap) {
        System.out.println("Your list of activities to do are:");
        for (TodoListEntryActivity activity : todoListEntryHashMap.keySet()) {
            System.out.println(activity.getActivity());
        }

    }

    // EFFECTS: Prints out list and all its contents with index number
    public void printEveryTodoArrayEntry(List<TodoListEntry> todoArray) {
        int index = 0;

        for (TodoListEntry entryGeneral : todoArray) {
            System.out.println("[" + index + "] " + entryGeneral.getTodoInfoFormat());
            index++;
        }
    }

    @Override
    public void update(List<Event> addedEvents) {
//        try {
//            printCalendar(calendar, timeToSet);
//        } catch (IOException e) {
//            System.out.println("Error loading Google Calendar");
//        }
    }

//    public void printCalendar(Calendar calendar, long timeToSet) throws IOException {
//        DateTime now = new DateTime(timeToSet);
//        DateTime max = new DateTime(timeToSet + TodoListCalendar.RANGE_TO_SET_ACTIVITIES);
//        Events events = calendar.events().list("primary")
//                .setMaxResults(100)
//                .setTimeMin(now)
//                .setTimeMax(max)
//                .setOrderBy("startTime")
//                .setSingleEvents(true)
//                .execute();
//        List<Event> items = events.getItems();
//        if (items.isEmpty()) {
//            System.out.println("No upcoming events found.");
//        } else {
//            System.out.println("Upcoming events");
//            for (Event event : items) {
//                DateTime start = event.getStart().getDateTime();
//                if (start == null) {
//                    start = event.getStart().getDate();
//                }
//                System.out.printf("%s (%s)\n", event.getSummary(), start);
//            }
//        }
//    }
}

