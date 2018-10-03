package ui;

import Model.TodoListFile;
import Model.TodoList;

import java.util.ArrayList;
import java.util.Scanner;

public class UserInput {
    public static final int LOAD_LIST_OR_SAVE = 1;
    public static final int CREATE_NEW_LIST_OR_QUIT = 0;
    public static final String LOAD = "load";
    public static final String SAVE = "save";

    private Scanner scanner = new Scanner(System.in);
    private TodoListFile todoListFile = new TodoListFile();


    // EFFECTS: Tries to find any TodoList files, if found then prints out files and prompts user
    // to either load a TodoList (return name of TodoList file) or create a new one (return null)
    // else does nothing and returns null
    public String promptUserForLoad() {
        ArrayList<String> listTodoListNames = todoListFile.listFiles();

        if (listTodoListNames.size() != 0) {
            System.out.println("Todo List files found:");
            System.out.println("Please press:\n["+ LOAD_LIST_OR_SAVE +"] to load a TodoList from file\n" +
                    "["+ CREATE_NEW_LIST_OR_QUIT +"] to create a new TodoList");

            int choice = getUserChoiceForIO(LOAD);

            if (choice != CREATE_NEW_LIST_OR_QUIT) {
                return getTodoListToLoad(listTodoListNames);
            }
        }
        System.out.println("Creating a new TodoList!");
        return null;
    }

    // EFFECTS: Prints out TodoList file names
    private void printTodoListFileNames(ArrayList<String> listTodoListNames) {
        for (String todoListName : listTodoListNames ) {
            System.out.println("File name: " +todoListName);
        }
    }

    // EFFECTS: Continuously prompts user until proper choice for loading or creating new list is entered
    private int getUserChoiceForIO(String option) {
        int choice = scanInt();

        while (choice < 0 || choice > 1) {
            printPromptIO(option);
            choice = scanInt();
        }

        return choice;
    }

    private void printPromptIO(String option) {
        if (option.equals(LOAD)) {
            System.out.println("Please enter a valid number:[" + LOAD_LIST_OR_SAVE + "] to load a TodoList from file\n" +
                    "[" + CREATE_NEW_LIST_OR_QUIT + "] to create a new TodoList");
        }
        else {
            System.out.println("Please enter a valid number:[" + LOAD_LIST_OR_SAVE + "] to save TodoList to file\n" +
                    "[" + CREATE_NEW_LIST_OR_QUIT + "] to quit without saving");
        }
    }

    // EFFECTS: Continuously prompts user until valid TodoList file name is entered
    // and returns TodoList file name
    private String getTodoListToLoad(ArrayList<String> listTodoListNames) {
        String todoListName;

        printTodoListFileNames(listTodoListNames);
        System.out.println("Please enter the name for the TodoList entry you will want to load");

        todoListName = scanString();

        while (!listTodoListNames.contains(todoListName)) {
            System.out.println("Error: Please enter a valid TodoList file name.\nMake " +
                    "sure to type out entry exactly as seen.");
            printTodoListFileNames(listTodoListNames);
            todoListName = scanString();
        }

        System.out.println("\n");
        return todoListName;

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

        System.out.println("\nPlease enter the number that you would like to do next\n[" +TodoList.ADD_ENTRY+ "] add entry \n[" +
                TodoList.REMOVE_ENTRY+ "] remove an entry \n[" +TodoList.PRINT_OUT_LIST+ "] print " + "out your list " +
                "\n[" +TodoList.QUIT+ "] quit");
        do {
            choice = scanInt();
        } while(choice < TodoList.QUIT || choice > TodoList.PRINT_OUT_LIST); // checks for valid input

        return choice;
    }

    // EFFECTS: Prompts user for entry to add to list and returns the string that the user entered
    public String getUserEntryToAdd() {
        System.out.println("\nPlease enter a todo-list entry as follows:\nActivity, priority " +
                "(low, medium, or high), time needed (in hrs), due date");
        System.out.println("An example would be: Play Basketball, medium, 4");

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
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private String scanString() {
        return scanner.nextLine();
    }

}
