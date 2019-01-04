package Model;

import com.fasterxml.jackson.annotation.*;
import exceptions.InvalidInputException;
import exceptions.InvalidPriorityException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PriorityTodoListEntry.class, name = "priorityTodoListEntry"),
        @JsonSubTypes.Type(value = LeisureTodoListEntry.class, name = "leisureTodoListEntry"),
})
public abstract class TodoListEntry implements Comparable<TodoListEntry> {
    static final int HIGH = 1;
    static final int MEDIUM = 2;
    static final int LOW = 3;
    static final int NOT_VALID_PRIORITY = -1;

    public static final String HIGH_STRING = "High";
    private static final String MEDIUM_STRING = "Medium";
    private static final String LOW_STRING = "Low";

    @JsonProperty("dueDate") String dueDateString;
    @JsonProperty ("time") double time;
    @JsonProperty("activity") private String activity;
    @JsonProperty("occurrences") private int occurrences;
    @JsonIgnore TodoListEntryActivity  todoListEntryActivity;
    @JsonIgnore private TodoList todoList;
    @JsonIgnore private InputChecker inputChecker;
    @JsonIgnore protected LocalDate dueDate;
    @JsonIgnore protected int priority;
    @JsonIgnore String priorityString;


    public TodoListEntry(String activity, double time) {
        this.todoListEntryActivity = new TodoListEntryActivity(activity, this);
        this.time = time;
        inputChecker = new InputChecker();
        this.activity = activity;
        occurrences = 1;
    }

    public int getPriority() {
        return priority;
    }

    @JsonIgnore
    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getActivity() {
        return activity;
    }

    public void setPriority(String priority) throws InvalidPriorityException{
        int priorityInt = priorityStringToInt(priority);
        if(priorityInt != NOT_VALID_PRIORITY) {
            setPriority(priorityInt);
            priorityString = getPriorityLevel();
        } else {
            throw new InvalidPriorityException();
        }
    }

    public void setOccurences(int occurrences) {
        this.occurrences = occurrences;
    }

    public int getOccurrences() {
        return occurrences;
    }

    // EFFECTS: Returns the contents of TodoListEntry in form of a formatted string
    public abstract String getTodoInfo();

    // EFFECTS: Returns formatted contents of PriorityTodoListEntry with units contained
    public abstract String getTodoInfoFormat();

    public TodoListEntryActivity getTodoListEntryActivity() {
        return todoListEntryActivity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
        todoListEntryActivity.setActivity(activity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListEntry)) return false;
        TodoListEntry that = (TodoListEntry) o;
        return Double.compare(that.time, time) == 0 &&
                todoListEntryActivity.equals(that.todoListEntryActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoListEntryActivity, time);
    }

    // MODIFIES: this
    // EFFECTS: if modifiedEntry does not follow format, throws an InvalidInputException
    // otherwise parses entry, extracts an array of strings corresponding to modifiedEntry, and sets
    // the corresponding string to activity
    String[] extractActivityAndParseEntry(String modifiedEntry) throws InvalidInputException {
        inputChecker.inputFollowsFormat(modifiedEntry);
        String[] parsedEntry = inputChecker.parseUserEntryString(modifiedEntry);
        String newActivity = parsedEntry[0];

        this.todoListEntryActivity.setActivity(newActivity);
        activity = newActivity;
        modifyTodoListMapIfNeeded(newActivity);
        return parsedEntry;
    }

    private void modifyTodoListMapIfNeeded(String newActivity) {
        if (todoList != null) {
            todoList.getTodoListMap().remove(new TodoListEntryActivity(newActivity, null));
            todoList.getTodoListMap().put(todoListEntryActivity, this);
        }
    }

    // EFFECTS: Returns -1 if o is less in time than this, otherwise returns alphabetical
    // comparison of o todoListEntryActivity to this todoListEntryActivity
    int compareTodoListEntryTimeActivity(TodoListEntry o) {
        int compareTime = Double.compare(o.time, time);
        if (compareTime != 0)
            return compareTime;
        else
            return todoListEntryActivity.getActivity().
                    compareTo(o.getTodoListEntryActivity().getActivity());
    }

    protected int priorityStringToInt(String priority) {
        if (priority.equalsIgnoreCase("high")) {
            return HIGH;
        } else if (priority.equalsIgnoreCase("medium")) {
            return MEDIUM;
        } else if (priority.equalsIgnoreCase("low")){
            return LOW;
        } else {
            return NOT_VALID_PRIORITY;
        }
    }

    @JsonIgnore
    // REQUIRES: priority level from LOW-HIGH
    // EFFECTS: Takes an integer representation of priority and returns its string equivalent
    protected String getPriorityLevel() {
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

    public void setTodoListEntryActivity(TodoListEntryActivity todoListEntryActivity) {
        this.todoListEntryActivity = todoListEntryActivity;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        dueDateString = dueDate.toString();
    }

    @JsonIgnore
    // MODIFIES: this
    // EFFECTS: Tries to modify dueDate by parsing date, if a parse exception is thrown then catches
    // it and sets dueDate to 7 days from the current date
    void setDueDate(String date) {
        try {
            dueDate = LocalDate.parse(date);
        } catch(DateTimeParseException e) {
            System.out.println("\nError: Date entered was invalid, setting dueDate for a week from now!");
            dueDate = LocalDate.now().plusDays(PriorityTodoListEntry.DEFAULT_DUE_DATE);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getTime() {
        return time;
    }

    // MODIFIES: this
    // EFFECTS: sets the associated TodoList to this to null
    public void removeTodoList() {
        this.todoList = null;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    // MODIFIES: this
    // EFFECTS: Adds todoList associated with this to field and updates
    public void setTodoList(TodoList todoList) {
        if (this.todoList == null) {
            this.todoList = todoList;
            todoList.addToTodoArray(this);
        }
    }
}
