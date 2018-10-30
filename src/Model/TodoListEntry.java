package Model;

import java.io.Serializable;
import java.util.Objects;

public abstract class TodoListEntry implements Comparable<TodoListEntry>, Serializable {
    String activity;
    double time;
    TodoList todoList;

    public TodoListEntry(String activity, double time) {
        this.activity = activity;
        this.time = time;
    }

    // EFFECTS: Returns th  e contents of TodoListEntry in form of a formatted string
    public abstract String getTodoInfo();

    // EFFECTS: Returns the activity
    public String getActivity() {
        return activity;
    }

    // EFFECTS: Returns time needed for activity
    public double getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListEntry)) return false;
        TodoListEntry that = (TodoListEntry) o;
        return Double.compare(that.time, time) == 0 &&
                Objects.equals(activity, that.activity) &&
                Objects.equals(todoList, that.todoList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(activity, time);
    }


}
