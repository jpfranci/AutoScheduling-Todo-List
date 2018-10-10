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
    public static final String EMPTY_DATE = "";

    private ArrayList<PriorityTodoListEntry> priorityTodoArray = new ArrayList<>();
    private ArrayList<LeisureTodoListEntry> leisureTodoArray = new ArrayList<>();
    private UserInput userInput = new UserInput();

    // constructor used for testing
    public TodoList(int choice) {
    }

    // MODIFIES: this
    // EFFECTS: Loads a TodoList file depending on user input and continuously prompts user to choose action
    // for TodoList until they choose quit, then quits program and either saves it or quits without saving
    public TodoList() {
        tryToLoad();
        handleCommand();
        tryToSave();
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to choose action for TodoList: add entry, remove entry, print out list, or
    // quit program and saves it
    // if list is empty, automatically prompts to add an entry
    private void handleCommand() {
        int choice;

        if (priorityTodoArray.size() == 0 && leisureTodoArray.size() == 0) {
            addTodoListEntry();
        }

        do {
            choice = userInput.promptUserForChoice();

            switch (choice) {
                case ADD_ENTRY:
                    addTodoListEntry();
                    sortPriorityTodoListByPriorityThenDateThenTime();
                    sortLeisureTodoList();
                    break;

                case REMOVE_ENTRY:
                    removeEntryIfPossible();
                    break;

                case PRINT_OUT_LIST:
                    printTodoList();
                    break;
            }

        } while(choice != QUIT);
    }

    private void removeEntryIfPossible() {
        if (priorityTodoArray.size() == 0 && leisureTodoArray.size() == 0) {
            System.out.println("Your lists are empty! Returning to choice menu!");
        }
        else {
            int removeChoice = userInput.getUserEntryType(UserInput.REMOVE);
            tryToRemoveEntry(removeChoice);
        }
    }

    // MODIFIES: this
    // EFFECTS: Gets user input to remove from TodoList and removes if valid
    private void tryToRemoveEntry(int removeChoice) {
        if (removeChoice == UserInput.PRIORITY_ENTRY && priorityTodoArray.size() != 0) {
            printEveryPriorityEntry();
            promptRemoveEntry(removeChoice);
        }

        else if (removeChoice == UserInput.LEISURE_ENTRY && leisureTodoArray.size() != 0) {
            printEveryLeisureEntry();
            promptRemoveEntry(removeChoice);
        }
    }

    private void promptRemoveEntry(int removeChoice) {
        int entryToRemove = userInput.getUserEntryToRemove();
        removeTodoListEntry(entryToRemove, removeChoice);
    }

    // EFFECTS: Prompts user if they want to save TodoList or quit without saving and saves
    // TodoList into file
    private void tryToSave() {
        String todoListNameForIO = userInput.promptUserToSave();
        if (todoListNameForIO != null) {
            save(todoListNameForIO);
        }
    }

    // MODIFIES: this
    // EFFECTS: Looks at DESTINATION folder and sees if there are valid TodoList files to load
    // if there are prompts the user if they would like to load one
    // and modifies this to be equal to the contents of the file
    private void tryToLoad() {
        String todoListNameForIO = userInput.promptUserForLoad();
        if (todoListNameForIO != null) {
            load(todoListNameForIO);
        }
    }

    // MODIFIES: this
    // EFFECTS: Continuously prompts user to add a TodoListEntry until they add a valid entry
    private void addTodoListEntry() {
        String userEntry;
        String date;
        int choice = userInput.getUserEntryType(UserInput.ADD);

        if (choice == UserInput.LEISURE_ENTRY) {
            do {
                userEntry = userInput.getLeisureUserEntryToAdd();
            } while (!tryToAddTodoListEntry(userEntry, EMPTY_DATE));
        }

        else {
            do {
                userEntry = userInput.getPriorityUserEntryToAdd();
                date = userInput.getUserEntryForDate();
            }
            while (!tryToAddTodoListEntry(userEntry, date));
        }
    }

    // MODIFIES: this
    // EFFECTS: Returns true if String matches format required and adds PriorityTodoListEntry represented by
    // String into TodoList
    public boolean tryToAddTodoListEntry(String userEntry, String date) {
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
        String userEntries[] = userEntry.split(", ");

        if (userEntries.length == 2) {
            leisureTodoListEntryParse(userEntries);
        }

        else {
            priorityTodoListEntryParse(date, userEntries);
        }
    }

    private void leisureTodoListEntryParse(String[] userEntries) {
        double time = Double.parseDouble(userEntries[1]);
        LeisureTodoListEntry leisureTodoListEntry = new  LeisureTodoListEntry(userEntries[0], time);

        leisureTodoArray.add(leisureTodoListEntry);
        System.out.println("Successfully added:\n" + leisureTodoListEntry.getActivity() +
                ", and which will take " + leisureTodoListEntry.getTime() + " hrs to complete ");
    }

    private void priorityTodoListEntryParse(String date, String[] userEntries) {
        PriorityTodoListEntry priorityTodoListEntry;
        double time = Double.parseDouble(userEntries[2]);

        priorityTodoListEntry = new PriorityTodoListEntry(userEntries[0], userEntries[1], time, date);
        priorityTodoArray.add(priorityTodoListEntry);

        System.out.println("Successfully added:\n" + priorityTodoListEntry.getActivity() +
                ", priority level " + priorityTodoListEntry.getPriorityLevel() +
                ", which will take " + priorityTodoListEntry.getTime() + " hrs to complete " +
                "and is due on " + "" + priorityTodoListEntry.getDueDate() + "!");
    }

    // EFFECTS: Checks if input follows the format of Activity, priority(high, medium, low), time
    // or if input follows format of Activity, time
    private boolean inputFollowsFormat(String userEntry) {
       return userEntry.matches("((\\w *)+, (?i)(high|medium|low), 0*[1-9].?[0-9]*)|" +
               "((\\w *)+, 0*[1-9].?[0-9]*)");
    }
    
    // MODIFIES: this
    // EFFECTS: Sorts TodoList by descending priority first and if equal priority then by due date
    // and if equal due date then by time needed to complete task
    public void sortPriorityTodoListByPriorityThenDateThenTime() {
        Collections.sort(priorityTodoArray);
    }

    // MODIFIES: this
    // EFFECTS: Sorts LeisureTodoList by descending time needed
    public void sortLeisureTodoList() {
        Collections.sort(leisureTodoArray);
    }

    // MODIFIES: this
    // EFFECTS: Returns true if successfully removed entry at index, returns false otherwise
    public boolean removeTodoListEntry(int indexToRemove, int removeChoice) {
        if (removeChoice == UserInput.PRIORITY_ENTRY &&
                indexToRemove < priorityTodoArray.size() && indexToRemove >= 0) {
            priorityTodoArray.remove(indexToRemove);
            System.out.println("Successfully removed entry!");
            return true;
        }

        else if(removeChoice == UserInput.LEISURE_ENTRY &&
                indexToRemove < leisureTodoArray.size() && indexToRemove >= 0 ) {
            leisureTodoArray.remove(indexToRemove);
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
            System.out.println("\nYou have to do these priority entries:");
            printEveryPriorityEntry();
            System.out.println("\nYou have to do these leisure entries:");
            printEveryLeisureEntry();
        }

    // EFFECTS: Prints out priority list and all its contents with index number
    private void printEveryLeisureEntry() {
        int index = 0;

        System.out.println("\nLeisure List:");

        if (leisureTodoArray.size() == 0) {
            System.out.println("Your leisure list is empty\n");
        }

        else {
            for (LeisureTodoListEntry entry : leisureTodoArray) {
                System.out.println("[" + index + "] " + entry.getActivity() + " which will take "
                        + entry.getTime() + " hours.");
                index++;
            }
        }
    }

    // EFFECTS: Prints out priority list and all its contents with index number
    private void printEveryPriorityEntry() {
        int index = 0;

        System.out.println("\nPriority List:");

        if (priorityTodoArray.size() == 0) {
            System.out.println("Your priority list is empty\n");
        }

        else {
            for (PriorityTodoListEntry entry : priorityTodoArray) {
                System.out.println("[" + index + "] " + entry.getActivity() + " which is "
                        + entry.getPriorityLevel() + " priority and will take "
                        + entry.getTime() + " hours and is due on " + entry.getDueDate());
                index++;
            }
        }

    }

    public ArrayList<PriorityTodoListEntry> getPriorityTodoArray() {
        return priorityTodoArray;
    }

    public ArrayList<LeisureTodoListEntry> getLeisureTodoArray() {
        return leisureTodoArray;
    }

    @Override
    // REQUIRES: Valid file containing first only serialized ArrayList<PriorityTodoListEntry>
    // and second ArrayList<LeisureTodoListEntry>
    // MODIFIES: this
    // EFFECTS: loads file and generates list from its contents, prints out its contents if not empty
    public void load(String fileName) {
        try {
            FileInputStream inFile = new FileInputStream(TodoListFile.DIRECTORY
                    + File.separator + fileName);
            ObjectInputStream inputStream = new ObjectInputStream(inFile);

            priorityTodoArray = (ArrayList<PriorityTodoListEntry>) inputStream.readObject();
            leisureTodoArray = (ArrayList<LeisureTodoListEntry>) inputStream.readObject();

            inputStream.close();
            inFile.close();

            if (priorityTodoArray.size() > 0 || leisureTodoArray.size() > 0) {
                System.out.println("Successfully loaded todo-list!");
                printEveryPriorityEntry();
                printEveryLeisureEntry();
            }
        }
        catch (IOException e) {
            System.out.println("Error: loading file! Creating new Todo-List");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Todo-list not found in file! Creating new Todo-List");
        }
    }

    @Override
    // EFFECTS: Saves state of TodoList in a file
    public void save(String fileName) {
        try {
            FileOutputStream outFile = new FileOutputStream(TodoListFile.DIRECTORY
                    + File.separator + fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(outFile);

            outputStream.writeObject(priorityTodoArray);
            outputStream.writeObject(leisureTodoArray);

            outputStream.close();
            outFile.close();
            System.out.println("Successfully saved todo-list to " +TodoListFile.DIRECTORY
                    + File.separator + fileName+ "!");
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: File could not be found!");
        }
        catch (IOException e) {
            System.out.println("Error: File could not be created or found!");
        }

    }
}
