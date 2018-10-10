package Model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PriorityTodoListEntry extends TodoListEntry {
    private static final int HIGH = 1;
    private static final int MEDIUM = 2;
    private static final int LOW = 3;
    public static final int DEFAULT_DUE_DATE = 7;

    public static final String HIGH_STRING = "High";
    public static final String MEDIUM_STRING = "Medium";
    public static final String LOW_STRING = "Low";

    private int priority;
    private LocalDate dueDate;


    // REQUIRES: priority to be one of low, medium, high
    // MODIFIES: this
    // EFFECTS: Constructs a PriorityTodoListEntry using activity, priority, and time information
    // called to constructor
    // if date is null then sets dueDate as week after today otherwise sets as date
    public PriorityTodoListEntry(String activity, String priority, double time, String date) {
        super(activity, time);
        this.priority = priorityStringToInt(priority);
        try {
            dueDate = LocalDate.parse(date);
        }
        catch(DateTimeParseException e) {
            System.out.println("\nError: Date entered was invalid, setting dueDate for a week from now!");
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

    @Override
    // EFFECTS: Returns contents of PriorityTodoListEntry in form of Activity,
    // priorityLevel(low, medium, high), time(in hrs), due date(yyyy-mm-dd)
    public String getTodoInfo() {
        return activity+ ", " +this.getPriorityLevel()+ ", " +time+ ", " +dueDate;
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
    // EFFECTS: checks if object is a PriorityTodoListEntry and if it is equal to this
    public boolean equals(Object obj) {
        if (obj instanceof PriorityTodoListEntry) {
            PriorityTodoListEntry entry = (PriorityTodoListEntry) obj;
            return activity.equals(entry.getActivity())
                    && priority == entry.getPriority()
                    && time == entry.getTime()
                    && dueDate.equals(entry.getDueDate());
        }
        return false;
    }

    @Override
    // EFFECTS: Follows PriorityTodoListEntry order to compare (order goes descending priority for
    // priority (high, medium, low), ascending order for dates, and descending order for time)
    // returns 1 if o is ahead in PriorityTodoListEntry order than this, 0 if equal, and -1 if behind in order
    public int compareTo(TodoListEntry o) {
        if(o instanceof PriorityTodoListEntry) {
            PriorityTodoListEntry entry = (PriorityTodoListEntry) o;

            int comparePriority = Integer.compare(priority, entry.getPriority());

            if (comparePriority != 0) {
                return comparePriority;
            }

            else {
                int compareDate = dueDate.compareTo(entry.getDueDate());
                if (compareDate != 0) {
                    return compareDate;
                }

                else {
                    return Double.compare(o.getTime(), time);
                }
            }

        }
        return 0;
    }

}
