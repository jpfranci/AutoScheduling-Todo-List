package Model;

import java.time.LocalDate;
import java.io.*;

public class TodoListEntry implements Comparable<TodoListEntry>, Serializable {
    private static final int HIGH = 1;
    private static final int MEDIUM = 2;
    private static final int LOW = 3;
    public static final int DEFAULT_DUE_DATE = 7;

    public static final String HIGH_STRING = "High";
    public static final String MEDIUM_STRING = "Medium";
    public static final String LOW_STRING = "Low";

    private String activity;
    private int priority;
    private double time;
    private LocalDate dueDate;


    // REQUIRES: priority to be one of low, medium, high & dueDate to not be Feb. 29
    // MODIFIES: this
    // EFFECTS: Constructs a TodoListEntry using activity, priority, and time information
    // called to constructor
    // if date is null then sets dueDate as week after today otherwise sets as date
    public TodoListEntry(String activity, String priority, double time, String date) {
        this.activity = activity;
        this.time = time;
        this.priority = priorityStringToInt(priority);
        if (date != null) {
            dueDate = LocalDate.parse(date);
        }
        else {
            dueDate = LocalDate.now().plusDays(DEFAULT_DUE_DATE);
        }
    }

    private int priorityStringToInt(String priority) {
        if (priority.equalsIgnoreCase("high")) {
            return HIGH;
        }
        else if (priority.equalsIgnoreCase("medium")) {
            return MEDIUM;
        }
        else {
            return LOW;
        }
    }

    // EFFECTS: Returns contents of TodoListEntry in form of Activity,
    // priorityLevel(low, medium, high), time(in hrs)
    public String getTodoInfo() {
        return activity+ ", " +this.getPriorityLevel()+ ", " +time+ ", " +dueDate;
    }


    // EFFECTS: Returns the activity
    public String getActivity() {
        return activity;
    }

    // EFFECTS: Returns time needed for activity
    public double getTime() {
        return time;
    }

    // EFFECTS: Return integer representation of priority
    public int getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // REQUIRES: priority level from LOW-HIGH
    // EFFECTS: Takes an integer representation of priority and returns its string equivalent
    public String getPriorityLevel() {
        switch (priority) {
            case HIGH:
                return HIGH_STRING;
            case MEDIUM:
                return MEDIUM_STRING;
            case LOW:
                return LOW_STRING;
        }
        return LOW_STRING;
    }

    @Override
    // EFFECTS: checks if object is a TodoListEntry and if it is equal to this
    public boolean equals(Object obj) {
        if (obj instanceof TodoListEntry) {
            TodoListEntry entry = (TodoListEntry) obj;
            return activity.equals(entry.getActivity())
                    && priority == entry.getPriority()
                    && time == entry.getTime()
                    && dueDate.equals(entry.getDueDate());
        }
        return false;
    }

    @Override
    // EFFECTS: Follows TodoListEntry order to compare (order goes descending priority for
    // priority (high, medium, low), ascending order for dates, and descending order for time)
    // returns 1 if o is ahead in TodoListEntry order than this, 0 if equal, and -1 if behind in order
    public int compareTo(TodoListEntry o) {
        int comparePriority = Integer.compare(priority, o.getPriority());

        if (comparePriority != 0) {
            return comparePriority;
        }

        else {
            int compareDate = dueDate.compareTo(o.getDueDate());
            if (compareDate != 0) {
                return compareDate;
            }

            else {
                return Double.compare(o.getTime(), time);
            }
        }
    }

}
