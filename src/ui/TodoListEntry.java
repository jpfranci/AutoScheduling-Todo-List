package ui;

public class TodoListEntry {
    private static final int HIGH = 3;
    private static final int MEDIUM = 2;
    private static final int LOW = 1;

    private String activity;
    private int priority;
    private double time;



    // constructs a TodoListEntry object
    public TodoListEntry(String activity, String priority, double time) {
        this.activity = activity;
        this.time = time;

        if (priority.equalsIgnoreCase("high")) {
            this.priority = HIGH;
        }
        else if (priority.equalsIgnoreCase("medium")) {
            this.priority = MEDIUM;
        }
        else {
            this.priority = LOW;
        }
    }

    // getters
    public String getActivity() {
        return activity;
    }

    public double getTime() {
        return time;
    }

    public String getPriorityLevel() {
        if (priority == HIGH) {
            return "High";
        }

        else if (priority == MEDIUM) {
            return "Medium";
        }

        else {
            return "Low";
        }
    }

}
