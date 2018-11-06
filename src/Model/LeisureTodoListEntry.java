package Model;

import exceptions.InvalidInputException;

public class LeisureTodoListEntry extends TodoListEntry {
    public LeisureTodoListEntry(String activity, double time) {
        super(activity, time);
    }

    @Override
    // EFFECTS: Returns contents of LeisureTodoListEntry in form of Activity, time(in hrs)
    public String getTodoInfo() {
        return activity+ ", " +time;
    }

    @Override
    // EFFECTS Returns formatted contents of LeisureTodoListEntry with units attached
    public String getTodoInfoFormat() {
        return activity + " which will take " + time + " hours.";
    }

    @Override
    // EFFECTS: checks if object is a LeisureTodoListEntry and if it is equal to this
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj.getClass() == getClass()) {
            LeisureTodoListEntry entry = (LeisureTodoListEntry) obj;
            return activity.equals(entry.getActivity())
                    && time == entry.getTime();
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Takes a string signifying the user's modified entry and then
    // parses it and modifies it to this, returns true if successful
    // returns false if InvalidInputException is caught
    public boolean modifyEntry(String modifiedEntry) {
        try{
            String[] parsedEntry = extractActivity(modifiedEntry);
            time = Double.parseDouble(parsedEntry[1]);
            addToTodoListMapIfNeeded();
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
        if (o instanceof PriorityTodoListEntry) {
            return 1;
        } else if (o instanceof LeisureTodoListEntry) {
            return Double.compare(o.getTime(), time);
        }
        return 0;
    }

}
