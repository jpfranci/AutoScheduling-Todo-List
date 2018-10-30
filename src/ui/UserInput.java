package ui;

import Model.TodoListFile;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInput {
    private static final int LOAD_LIST_OR_SAVE = 1;
    private static final int CREATE_NEW_LIST_OR_QUIT = 0;
    private static final String LOAD = "load";
    private static final String SAVE = "save";
    private static final int NON_SUCCESSFUL_INT = -1;
    public static final int LEISURE_ENTRY = 0;
    public static final int PRIORITY_ENTRY = 1;
    public static final String ADD = "add";
    public static final String REMOVE = "remove";

    private Scanner scanner = new Scanner(System.in);
    private ListPrinter listPrinter;

    public void setListPrinter(ListPrinter listPrinter) {
        this.listPrinter = listPrinter;
    }

    // EFFECTS: Tries to find any TodoList files, if found then prints out files and prompts user
    // to either load a TodoList (return name of TodoList file) or create a new one (return null)
    // else does nothing and returns null
    public String promptUserForLoad(ArrayList<String> listTodoListNames) {
        System.out.println("Todo List files found:");
        System.out.println("Please press:\n[" + LOAD_LIST_OR_SAVE + "] to load a TodoList from file\n" +
                "[" + CREATE_NEW_LIST_OR_QUIT + "] to create a new TodoList");

        int choice = getUserChoiceForIO(LOAD);

        if (choice != CREATE_NEW_LIST_OR_QUIT) {
            return getTodoListToLoad(listTodoListNames);
        }
        System.out.println("Creating a new TodoList!");
        return null;
    }

    // EFFECTS: Continuously prompts user until proper choice for loading or creating new list is entered
    private int getUserChoiceForIO(String option) {
        int choice = scanInt();

        while (choice < CREATE_NEW_LIST_OR_QUIT || choice > LOAD_LIST_OR_SAVE) {
            printPromptIO(option);
            choice = scanInt();
        }

        return choice;
    }

    private void printPromptIO(String option) {
        if (option.equals(LOAD)) {
            System.out.println("Please enter a valid number:[" + LOAD_LIST_OR_SAVE + "] to load a" +
                    " TodoList from file\n" + "[" + CREATE_NEW_LIST_OR_QUIT + "] " +
                    "to create a new TodoList");
        }
        else {
            System.out.println("Please enter a valid number:[" + LOAD_LIST_OR_SAVE + "]" +
                    " to save TodoList to file\n" + "[" + CREATE_NEW_LIST_OR_QUIT + "] " +
                    "to quit without saving");
        }
    }

    // EFFECTS: Continuously prompts user until valid TodoList file name is entered
    // and returns TodoList file name
    private String getTodoListToLoad(ArrayList<String> listTodoListNames) {
        String todoListName;

        listPrinter.printTodoListFileNames(listTodoListNames);
        System.out.println("Please enter the name for the TodoList entry you will want to load");

        todoListName = scanString();

        while (!listTodoListNames.contains(todoListName)) {
            System.out.println("Error: Please enter a valid TodoList file name.\nMake " +
                    "sure to type out entry exactly as seen.");
            listPrinter.printTodoListFileNames(listTodoListNames);
            todoListName = scanString();
        }

        System.out.println("\n");
        return todoListName;

    }

    // EFFECTS: Continuously prompts user for what type of entry they would like to enter or remove
    // returns if type is valid
    public int getUserEntryType(String type) {
        int choice;

            do {
                System.out.println("Please enter:\n[" + LEISURE_ENTRY + "] to "+type+" a leisure entry " +
                        "(only activity " + "and time needed)\n[" + PRIORITY_ENTRY + "] to " +type+"" +
                        " a priority entry (activity, time, " + "priority and date needed)");
                choice = scanInt();
            }
            while (choice < LEISURE_ENTRY || choice > PRIORITY_ENTRY);

        return choice;
}

    // EFFECTS: Prompts user to choose between saving TodoList to file or to quit without saving
    public String promptUserToSave() {
        System.out.println("Please press:\n["+ LOAD_LIST_OR_SAVE +"] to save a TodoList to file\n" +
                "["+ CREATE_NEW_LIST_OR_QUIT +"] to quit without saving");
        int choice = getUserChoiceForIO(SAVE);

        if (choice != CREATE_NEW_LIST_OR_QUIT) {
            return getFileNameForSaving();
        }
        return null;
    }

    // EFFECTS: Prompts user to enter a valid file name (only letters or digits) and returns
    // file name with file extension attached
    private String getFileNameForSaving() {
        String userChoiceForSaveFile;
        System.out.println("Please enter a file name for saving (ex. todolist)");

        userChoiceForSaveFile = scanString();

        while(!userChoiceForSaveFile.matches("\\w+")) {
            System.out.println("Enter a valid file name that only contains numbers or letters");
            userChoiceForSaveFile = scanString();
        }

        return userChoiceForSaveFile.concat(TodoListFile.FILE_EXTENSION);
    }

    // EFFECTS: Prompts user to choose between adding entry, removing entry, and quitting entry and returns
    // the user's choice
    public int promptUserForChoice() {
        int choice;

        System.out.println("\nPlease enter the number that you would like to do next\n[" + CommandHandler.ADD_ENTRY+ "] add entry \n[" +
                CommandHandler.REMOVE_ENTRY+ "] remove an entry \n[" + CommandHandler.PRINT_OUT_LIST+ "] print " + "out your list " +
                "\n[" + CommandHandler.QUIT+ "] quit");
        do {
            choice = scanInt();
        } while(choice < CommandHandler.QUIT || choice > CommandHandler.PRINT_OUT_LIST); // checks for valid input

        return choice;
    }

    // EFFECTS: Prompts user for entry to add to list and returns the string that the user entered
    public String getPriorityUserEntryToAdd() {
        System.out.println("\nPlease enter a todo-list entry as follows:\nActivity, priority " +
                "(low, medium, or high), time needed (in hrs)");
        System.out.println("An example would be: Play Basketball, medium, 4");

        return scanString();
    }

    // EFFECTS: Prompts user for entry to add to list and returns the string that the user entered
    public String getLeisureUserEntryToAdd() {
        System.out.println("\nPlease enter a todo-list entry as follows:\nActivity, time needed (in hrs)");
        System.out.println("An example would be: Play Basketball, 4");

        return scanString();
    }

    public String getUserEntryForDate() {
        System.out.println("\nPlease enter the due date for the activity as follows: \n" +
                "YYYY-MM-DD (due dates are set a week from today's date by default)\n" +
                "(entering nothing or entering an invalid format will result in default due date)");

        return scanString();
    }

    // EFFECTS: Prompts user for entry to remove from list and returns the number that user entered
    public int getUserEntryToRemove() {
        System.out.println("\nPlease enter the number of the entry you would like to remove");

        return scanInt();
    }

    private int scanInt() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            System.out.println("Input is not a valid integer. Please try again");
            scanner.nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Input is not an integer. Please try again!");
            scanner.nextLine();
        }
        return NON_SUCCESSFUL_INT;
    }

    private String scanString() {
        return scanner.nextLine();
    }

}
