package Model;

import java.util.Objects;

public class TodoListEntryActivity implements Comparable<TodoListEntryActivity> {
    private String activity;
    private TodoListEntry todoListEntry;

    public TodoListEntryActivity(String activity, TodoListEntry todoListEntry) {
        this.activity = activity;
        this.todoListEntry = todoListEntry;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    // EFFECTS: Returns true if this is equal to o or this activity is equal to o activity
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListEntryActivity)) return false;
        TodoListEntryActivity that = (TodoListEntryActivity) o;
        return Objects.equals(activity, that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity);
    }

    // EFFECTS: Returns 0 if this activity equals o's activity or this' todolistEntry or o's todoListEntry
    // is null otherwise returns the result of comparing the this TodolistEntry to o TodolistEntry
    @Override
    public int compareTo(TodoListEntryActivity o) {
        if (this.equals(o)) {
            return 0;
        } else if (todoListEntry == null || o.todoListEntry == null) {
            return 0;
        } else {
            return todoListEntry.compareTo((o.todoListEntry));
        }
    }
}
