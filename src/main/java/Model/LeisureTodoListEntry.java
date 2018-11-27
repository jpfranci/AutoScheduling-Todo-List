package Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.InvalidInputException;


public class LeisureTodoListEntry extends TodoListEntry {
    // MODIFIES: this
    // EFFECTS: Constructs a LeisureTodoListEntry and sets this activity to activity
    // and this time to time
    public LeisureTodoListEntry(@JsonProperty("activity") String activity,
                                @JsonProperty("time") double time) {
        super(activity, time);
    }

    @JsonCreator
    // MODIFIES: this
    // EFFECTS: Constructs a PriorityTodoListEntry using the natural integer representation of priority,
    // the string representation of todoListEntryActivity, and time information
    // if date is not valid then sets dueDate as week after today otherwise sets as date
    public LeisureTodoListEntry(@JsonProperty("activity") String activity, @JsonProperty("priority")
            int priority, @JsonProperty("time") double time, @JsonProperty("dueDate")String date) {
        super(activity, time);
        this.priority = priority;
        priorityString = getPriorityLevel();
        dueDateString = date;
        setDueDate(date);
    }

    @Override
    @JsonIgnore
    // EFFECTS: Returns contents of LeisureTodoListEntry in form of Activity, time(in hrs)
    public String getTodoInfo() {
        return todoListEntryActivity.getActivity() + ", " +time;
    }

    @Override
    @JsonIgnore
    // EFFECTS Returns formatted contents of LeisureTodoListEntry with units attached
    public String getTodoInfoFormat() {
        return todoListEntryActivity.getActivity() + " which will take " + time + " hours.";
    }

    // MODIFIES: this
    // EFFECTS: Takes a string signifying the user's modified entry and then
    // parses it and modifies it to this, returns true if successful
    // returns false if InvalidInputException is caught
    public boolean modifyEntry(String modifiedEntry) {
        try{
            String[] parsedEntry = extractActivityAndParseEntry(modifiedEntry);
            time = Double.parseDouble(parsedEntry[1]);
        } catch(InvalidInputException e) {
            System.out.println("Invalid input! Try again");
            return false;
        }
        return true;
    }

    @Override
    // EFFECTS: Follows LeisureTodoListEntry in order to compare to this. If time is greater for this than
    // o then returns 1, if equal then returns 0, else returns -1
    public int compareTo(TodoListEntry o) {
        if(o.todoListEntryActivity.equals(todoListEntryActivity))
            return 0;

        if (o instanceof PriorityTodoListEntry) {
            return 1;
        } else if (o instanceof LeisureTodoListEntry) {
            return compareTodoListEntryTimeActivity(o);
        }
        return 0;
    }


}
