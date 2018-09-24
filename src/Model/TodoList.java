package Model;

import ui.UserInput;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TodoList {
    public static final int QUIT = 0;
    public static final int ADD_ENTRY = 1;
    public static final int REMOVE_ENTRY = 2;
    public static final int PRINT_OUT_LIST = 3;

    private ArrayList<TodoListEntry> todoArray = new ArrayList<>();
    private UserInput userInput = new UserInput();

    // constructor used for testing
    public TodoList(int choice) {
    }

    public TodoList() {
        todoListAction();
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to choose action for TodoList: add entry, remove entry, print out list,
    // or quit program if list is empty, automatically prompts to add an entry
    public void todoListAction() {
        String userEntry;
        int choice;

        if (todoArray.size() == 0) {
            do {
                userEntry = userInput.getUserEntryToAdd();
            }
            while(!addTodoListEntry(userEntry));
        }

        do {
            choice = userInput.promptUserForChoice();

            switch (choice) {
                case ADD_ENTRY:
                    do {
                        userEntry = userInput.getUserEntryToAdd();
                    }
                    while(!addTodoListEntry(userEntry));
                    sortTodoListByDescendingPriorityAndTime();
                    break;
                case REMOVE_ENTRY:
                    printEveryEntry();
                    int entryToRemove = userInput.getUserEntryToRemove();
                    removeTodoListEntry(entryToRemove);
                    break;
                case PRINT_OUT_LIST:
                    printTodoList();
                    break;
            }

        } while(choice != QUIT);
    }

    // MODIFIES: this
    // EFFECTS: Returns true if String matches format required and adds TodoListEntry represented by
    // String into todolist
    public boolean addTodoListEntry(String userEntry) {
       if (inputFollowsFormat(userEntry)) {
           parseString(userEntry);
           return true;
       }

       else {
           System.out.println("Invalid input: please try again!");
           return false;
       }
    }

    // REQUIRES: Valid string that follows format of: Activity, Priority(low, medium, high), time(number in hrs)
    // MODIFIES: this
    // EFFECTS: Splits string into Activity, priority, and time and generates a todoListEntry
    // returns array of strings that represents the split strings
    public String[] parseString(String userEntry) {
        String userEntries[] = userEntry.split(", ");

        int time = Integer.parseInt(userEntries[2]);

        TodoListEntry todoListEntry = new TodoListEntry(userEntries[0], userEntries[1], time);

        todoArray.add(todoListEntry);
        System.out.println("Successfully added:\n" + userEntries[0] + ", priority level "
                + userEntries[1] + ", which will take " + time + " hrs to complete!\n");

        return userEntries;
    }

    private boolean inputFollowsFormat(String userEntry) {
       return userEntry.matches("(\\w *)+, (?i)(high|medium|low), 0*[1-9][0-9]*");
    }

    // MODIFIES: this
    // EFFECTS: Sorts TodoList by descending priority first and if equal priority then by
    // time needed to complete task
    public void sortTodoListByDescendingPriorityAndTime() {
        Collections.sort(todoArray, new Comparator<TodoListEntry>() {
            public int compare(TodoListEntry e1, TodoListEntry e2) {
                int comparePriority = Integer.compare(e1.getPriority(), e2.getPriority());
                if (comparePriority != 0) {
                    return comparePriority;
                }
                else {
                    return Double.compare(e2.getTime(), e1.getTime());
                }
            }
        });
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

        for (TodoListEntry entry : todoArray) {
            System.out.println("["+index+"] " +entry.getActivity() + " which is " + entry.getPriorityLevel() +
                    " priority and will take " + entry.getTime() + " hours");
            index++;
        }
    }

    public ArrayList<TodoListEntry> getTodoArray() {
        return todoArray;
    }

}
