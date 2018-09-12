package ui;

public class TodoListEntry {
    private String activity;
    private int priority;
    private double time;

    // constructs a TodoListEntry object
    public TodoListEntry(String activity, int priority, double time) {
        this.activity = activity;
        this.priority = priority;
        this.time = time;
    }


    // getters
    public String getActivity() {
        return activity;
    }

    public double getTime() {
        return time;
    }

    public String getPriorityLevel() {
        if (priority >= 4) {
            return "High";
        }

        else if (priority >= 2) {
            return "Medium";
        }

        else {
            return "Low";
        }
    }

}
