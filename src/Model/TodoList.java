package Model;

import ui.UserInput;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class TodoList implements Loadable, Saveable {
    public static final int QUIT = 0;
    public static final int ADD_ENTRY = 1;
    public static final int REMOVE_ENTRY = 2;
    public static final int PRINT_OUT_LIST = 3;
    public static final String IO_FILE = "todolist.ser";

    private ArrayList<TodoListEntry> todoArray = new ArrayList<>();
    private UserInput userInput = new UserInput();

    // constructor used for testing
    public TodoList(int choice) {
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to choose action for TodoList: add entry, remove entry, print out list,
    // or quit program and saves it
    // if list is empty, automatically prompts to add an entry
    public TodoList() {
        tryToLoad();

        if (todoArray.size() == 0) {
            addEntry();
        }

        handleCommand();
        tryToSave();
    }

    private void handleCommand() {
        int choice;
        do {
            choice = userInput.promptUserForChoice();

            switch (choice) {
                case ADD_ENTRY:
                    addEntry();
                    sortTodoListByPriorityThenDateThenTime();
                    break;

                case REMOVE_ENTRY:
                    printEveryEntry();
                    tryToRemoveEntry();
                    break;

                case PRINT_OUT_LIST:
                    printTodoList();
                    break;
            }

        } while(choice != QUIT);
    }

    private void tryToRemoveEntry() {
        if (todoArray.size() != 0) {
            int entryToRemove = userInput.getUserEntryToRemove();
            removeTodoListEntry(entryToRemove);
        }
    }

    private void tryToSave() {
        String todoListNameForIO;
        todoListNameForIO = userInput.promptUserToSave();
        if (todoListNameForIO != null) {
            save(todoListNameForIO);
        }
    }

    private void tryToLoad() {
        String todoListNameForIO;
        todoListNameForIO = userInput.promptUserForLoad();
        if (todoListNameForIO != null) {
            load(todoListNameForIO);
        }
    }

    private void addEntry() {
        String userEntry;
        String date;
        do {
            userEntry = userInput.getUserEntryToAdd();
            date = userInput.getUserEntryForDate();
        }
        while(!addTodoListEntry(userEntry, date));
    }

    // MODIFIES: this
    // EFFECTS: Returns true if String matches format required and adds TodoListEntry represented by
    // String into TodoList
    public boolean addTodoListEntry(String userEntry, String date) {
       if (inputFollowsFormat(userEntry)) {
           parseString(userEntry, date);
           return true;
       }

       else {
           System.out.println("Invalid input for activity, priority, time: please try again!");
           return false;
       }
    }

    // REQUIRES: Valid string that follows format of: Activity, Priority(low, medium, high), time(number in hrs)
    // MODIFIES: this
    // EFFECTS: Splits string into Activity, priority, and time and generates a todoListEntry
    // checks if date is valid and if it is not then sets todoListEntry due date as one week from today's date
    // and if valid then sets due date as that date
    private void parseString(String userEntry, String date) {
        TodoListEntry todoListEntry;
        String userEntries[] = userEntry.split(", ");

        int time = Integer.parseInt(userEntries[2]);
        if (dateFollowsFormat(date)) {
            todoListEntry = new TodoListEntry(userEntries[0], userEntries[1], time, date);
            todoArray.add(todoListEntry);
        }

        else {
            todoListEntry = new TodoListEntry(userEntries[0], userEntries[1], time, null);
            todoArray.add(todoListEntry);
        }

            System.out.println("Successfully added:\n" + todoListEntry.getActivity() +
                    ", priority level " + todoListEntry.getPriorityLevel() +
                    ", which will take " + todoListEntry.getTime() + " hrs to complete " +
                    "and is due on " + "" +todoListEntry.getDueDate()+ "!\n");
        }

    // EFFECTS: Checks if input follows the format of Activity, priority(high, medium, low), time
    private boolean inputFollowsFormat(String userEntry) {
       return userEntry.matches("(\\w *)+, (?i)(high|medium|low), 0*[1-9][0-9]*");
    }

    // EFFECTS: Checks if date is valid and is in the 2000s
    private boolean dateFollowsFormat(String date) {
        return date.matches("[2-9]\\d{3}-(0\\d|1[0-2])-(3[0-1]|[0-2]\\d)");
    }

    // MODIFIES: this
    // EFFECTS: Sorts TodoList by descending priority first and if equal priority then by due date
    // and if equal due date then by time needed to complete task
    public void sortTodoListByPriorityThenDateThenTime() {
        Collections.sort(todoArray);
    }

    // MODIFIES: this
    // EFFECTS: Returns true if successfully removed entry at index, returns false otherwise
    public boolean removeTodoListEntry(int indexToRemove) {
        if (indexToRemove < todoArray.size() && indexToRemove >= 0) {
            todoArray.remove(indexToRemove);
            System.out.println("Successfully removed entry!");
            return true;
        }

        else {
            System.out.println("Error: Invalid user entry number!");
            return false;
        }
    }

    // EFFECTS: Prints out List and its contents into screen with array index
    private void printTodoList() {
        if (todoArray.size() == 0) {
            System.out.println("Your todoArray-list is empty");
        }

        else {
            System.out.println("\nYou have to:");
            printEveryEntry();
        }
    }


    // EFFECTS: Prints out list and all its contents with index number
    private void printEveryEntry() {
        int index = 0;

        if (todoArray.size() == 0) {
            System.out.println("Your todoArray-list is empty");
        }

        else {
            for (TodoListEntry entry : todoArray) {
                System.out.println("[" + index + "] " + entry.getActivity() + " which is "
                        + entry.getPriorityLevel() + " priority and will take "
                        + entry.getTime() + " hours and is due on " + entry.getDueDate());
                index++;
            }
        }
    }

    public ArrayList<TodoListEntry> getTodoArray() {
        return todoArray;
    }

    @Override
    // REQUIRES: Valid file containing only serialized ArrayList<TodoListEntry>
    // MODIFIES: this
    // EFFECTS: loads file and generates list from its contents, prints out its contents if not empty
    public void load(String fileName) {
        try {
            FileInputStream inFile = new FileInputStream(fileName);
            ObjectInputStream inputStream = new ObjectInputStream(inFile);

            todoArray = (ArrayList<TodoListEntry>)inputStream.readObject();

            inputStream.close();
            inFile.close();

            if (todoArray.size() > 0) {
                System.out.println("Successfully loaded todo-list!");
                printEveryEntry();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found!");
            e.printStackTrace();
        }
    }

    @Override
    // EFFECTS: Saves state of TodoList in a file
    public void save(String fileName) {
        try {
            FileOutputStream outFile = new FileOutputStream(fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(outFile);

            outputStream.writeObject(todoArray);

            outputStream.close();
            outFile.close();
            System.out.println("Successfully saved todo-list!");
        }
        catch (FileNotFoundException e) {
            System.out.println("File could not be opened or not found!");
        }
        catch (IOException e) {
            System.out.println("File could not be opened!");
        }

    }
}
