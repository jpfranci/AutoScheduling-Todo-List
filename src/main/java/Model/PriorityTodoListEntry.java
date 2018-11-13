package Model;

import com.fasterxml.jackson.annotation.*;
import exceptions.InvalidInputException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;


public class PriorityTodoListEntry extends TodoListEntry {
    private static final int HIGH = 1;
    private static final int MEDIUM = 2;
    private static final int LOW = 3;
    public static final int DEFAULT_DUE_DATE = 7;

    public static final String HIGH_STRING = "High";
    private static final String MEDIUM_STRING = "Medium";
    private static final String LOW_STRING = "Low";
    @JsonIgnore
    private int priority;
    @JsonIgnore
    private LocalDate dueDate;
    @JsonProperty("dueDate")
    private String dueDateString;

    // REQUIRES: priority to be one of low, medium, high
    // MODIFIES: this
    // EFFECTS: Constructs a PriorityTodoListEntry using todoListEntryActivity, priority, and time information
    // called to constructor
    // if date is not valid then sets dueDate as week after today otherwise sets as date
    public PriorityTodoListEntry(String activity, String priority, double time, String date) {
        super(activity, time);
        this.priority = priorityStringToInt(priority);
        setDueDate(date);
        dueDateString = date;
    }

    @JsonCreator
    // MODIFIES: this
    // EFFECTS: Constructs a PriorityTodoListEntry using the natural integer representation of priority,
    // the string representation of todoListEntryActivity, and time information
    // if date is not valid then sets dueDate as week after today otherwise sets as date
    public PriorityTodoListEntry(@JsonProperty("activity") String activity, @JsonProperty("priority")
            int priority, @JsonProperty("time") double time, @JsonProperty("dueDate")String date) {
        super(activity, time);
        this.priority = priority;
        dueDateString = date;
        setDueDate(date);
    }

    @Override
    @JsonIgnore
    // EFFECTS: Returns formatted contents of PriorityTodoListEntry with units contained
    public String getTodoInfoFormat() {
        return todoListEntryActivity.getActivity() + " which is " +getPriorityLevel() + " priority and will take "
                +time + " hours and is due on " + dueDate;
    }

    // EFFECTS: Return integer representation of priority
    private int getPriority() {
        return priority;
    }

    @JsonIgnore
    public LocalDate getDueDate() {
        return dueDate;
    }

    @JsonIgnore
    // REQUIRES: priority level from LOW-HIGH
    // EFFECTS: Takes an integer representation of priority and returns its string equivalent
    private String getPriorityLevel() {
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

    @JsonIgnore
    // MODIFIES: this
    // EFFECTS: Tries to modify dueDate by parsing date, if a parse exception is thrown then catches
    // it and sets dueDate to 7 days from the current date
    private void setDueDate(String date) {
        try {
            dueDate = LocalDate.parse(date);
        } catch(DateTimeParseException e) {
            System.out.println("\nError: Date entered was invalid, setting dueDate for a week from now!");
            dueDate = LocalDate.now().plusDays(DEFAULT_DUE_DATE);
        }
    }

    private int priorityStringToInt(String priority) {
        if (priority.equalsIgnoreCase("high")) {
            return HIGH;
        } else if (priority.equalsIgnoreCase("medium")) {
            return MEDIUM;
        } else {
            return LOW;
        }
    }

    @Override
    @JsonIgnore
    // EFFECTS: Returns contents of PriorityTodoListEntry in form of Activity,
    // priorityLevel(low, medium, high), time(in hrs), due date(yyyy-mm-dd)
    public String getTodoInfo() {
        return todoListEntryActivity.getActivity()+ ", " +getPriorityLevel()+ ", " +time+ ", " +dueDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriorityTodoListEntry)) return false;
        if (!super.equals(o)) return false;
        PriorityTodoListEntry that = (PriorityTodoListEntry) o;
        return priority == that.priority &&
                Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), priority, dueDate);
    }

    @Override
    // EFFECTS: Follows PriorityTodoListEntry order to compare (order goes descending priority for
    // priority (high, medium, low), ascending order for dates, and descending order for time)
    // returns 1 if o is ahead in PriorityTodoListEntry order than this, 0 if equal, and -1 if behind in order
    public int compareTo(TodoListEntry o) {
        if (o instanceof LeisureTodoListEntry) {
            return -1;
        }
        else if (o instanceof PriorityTodoListEntry) {
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
                    return compareTodoListEntryTimeActivity(o);
                }
            }
        }
        return 0;
    }

    // MODIFIES: this
    // EFFECTS: Takes a string signifying the user's modified entry and then
    // parses it and modifies it to this, returns true if successful
    // returns false if InvalidInputException is caught
    public boolean modifyEntry(String modifiedEntry, String date) {
        try {
            String[] parsedEntry = extractActivityAndParseEntry(modifiedEntry);
            priority = priorityStringToInt(parsedEntry[1]);
            time = Double.parseDouble(parsedEntry[2]);
            setDueDate(date);
        } catch (InvalidInputException e) {
            System.out.println("Invalid input!");
            return false;
        }
        return true;
    }
}
