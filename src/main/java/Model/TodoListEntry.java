package Model;

import com.fasterxml.jackson.annotation.*;
import exceptions.InvalidInputException;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PriorityTodoListEntry.class, name = "priorityTodoListEntry"),
        @JsonSubTypes.Type(value = LeisureTodoListEntry.class, name = "leisureTodoListEntry"),
})
public abstract class TodoListEntry implements Comparable<TodoListEntry> {
    @JsonIgnore TodoListEntryActivity  todoListEntryActivity;
    @JsonIgnore private TodoList todoList;
    @JsonIgnore private InputChecker inputChecker;
    @JsonProperty ("time") double time;
    @JsonProperty("activity") private String activity;
    @JsonProperty("occurrences") private int occurrences;

    public TodoListEntry(String activity, double time) {
        this.todoListEntryActivity = new TodoListEntryActivity(activity, this);
        this.time = time;
        inputChecker = new InputChecker();
        this.activity = activity;
        occurrences = 1;
    }

    public String getActivity() {
        return activity;
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
