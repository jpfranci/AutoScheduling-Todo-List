package Model;

import exceptions.InvalidInputException;

import java.io.Serializable;

public class InputChecker implements Serializable{
    private static final int LENGTH_FIELDS_LEISURE_ENTRY = 2;

    // REQUIRES: Valid string that follows format of: Activity, Priority(low, medium, high), time(number in hrs)
    // EFFECTS: Splits string into Activity, priority, and time and generates a todoListEntry
    // checks if date is valid and if it is not then sets todoListEntry due date as one week from today's date
    // and if valid then sets due date as that date
    public TodoListEntry parseTodoListEntry(String userEntry, String date) {
        String userEntries[] = parseUserEntryString(userEntry);

        if (userEntries.length == LENGTH_FIELDS_LEISURE_ENTRY) {
            return leisureTodoListEntryParse(userEntries);
        } else {
            return priorityTodoListEntryParse(date, userEntries);
        }
    }

    // EFFECTS: Parses userEntry string separating by " ,"
    public String[] parseUserEntryString(String userEntry) {
        return userEntry.split(", ");
    }

    private TodoListEntry leisureTodoListEntryParse(String[] userEntries) {
        double time = Double.parseDouble(userEntries[1]);
        return new LeisureTodoListEntry(userEntries[0], time);
    }

    private TodoListEntry priorityTodoListEntryParse(String date, String[] userEntries) {
        double time = Double.parseDouble(userEntries[2]);
        return new PriorityTodoListEntry(userEntries[0], userEntries[1], time, date);
    }

    // EFFECTS: Checks if input follows the format of Activity, priority(high, medium, low), time
    // or if input follows format of Activity, time
    public boolean inputFollowsFormat(String userEntry) throws InvalidInputException {
        if(!userEntry.matches("((\\w *)+, (?i)(high|medium|low), 0*[1-9].?[0-9]*)|" +
                "((\\w *)+, 0*[1-9].?[0-9]*)")) {
            throw new InvalidInputException();
        }
        return true;
    }

}
