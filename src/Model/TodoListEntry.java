package Model;

import java.io.Serializable;

public abstract class TodoListEntry implements Comparable<TodoListEntry>, Serializable {
    protected String activity;
    protected double time;

    public TodoListEntry(String activity, double time) {
        this.activity = activity;
        this.time = time;
    }

    // EFFECTS: Returns the contents of TodoListEntry in form of a formatted string
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
    // EFFECTS: checks if object is equal to this
    public abstract boolean equals(Object obj);

    @Override
    // EFFECTS: returns 1 if o is ahead in TodoListEntry order than this,
    // 0 if equal, and -1 if behind in order
    public abstract int compareTo(TodoListEntry o);
}
