package Model;

import exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.Objects;

public abstract class TodoListEntry implements Comparable<TodoListEntry>, Serializable {
    String activity;
    double time;
    TodoList todoList;
    private InputChecker inputChecker = new InputChecker();

    public TodoListEntry(String activity, double time) {
        this.activity = activity;
        this.time = time;
    }

    // EFFECTS: Returns the contents of TodoListEntry in form of a formatted string
    public abstract String getTodoInfo();

    // EFFECTS: Returns formatted contents of PriorityTodoListEntry with units contained
    public abstract String getTodoInfoFormat();

    // EFFECTS: Returns the activity
    public String getActivity() {
        return activity;
    }

    // EFFECTS: Returns time needed for activity
    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListEntry)) return false;
        TodoListEntry that = (TodoListEntry) o;
        return Double.compare(that.time, time) == 0 &&
                Objects.equals(activity, that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, time);
    }

    protected String[] extractActivity(String modifiedEntry) throws InvalidInputException {
        inputChecker.inputFollowsFormat(modifiedEntry);
        String[] parsedEntry = inputChecker.parseUserEntryString(modifiedEntry);
        modifyTodoListMapIfNeeded();

        activity = parsedEntry[0];
        return parsedEntry;
    }

    private void modifyTodoListMapIfNeeded() {
        if(todoList!= null)
            todoList.getTodoListMap().remove(activity);
    }

    void addToTodoListMapIfNeeded() {
        if (todoList != null)
            todoList.getTodoListMap().put(activity, this);
    }

    // MODIFIES: this
    // EFFECTS: Adds todoList associated with this to field and updates
    public void setTodoList(TodoList todoList) {
        if (this.todoList == null) {
            this.todoList = todoList;
            todoList.addToTodoArray(this);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the associated TodoList to this to null
    public void removeTodoList() {
        this.todoList = null;
    }

    public TodoList getTodoList() {
        return todoList;
    }
}
