package ui;

import Model.*;

import java.util.ArrayList;
import java.util.Map;

public class CommandHandler {
    public static final int QUIT = 0;
    public static final int ADD_ENTRY = 1;
    public static final int REMOVE_ENTRY = 2;
    public static final int PRINT_OUT_LIST = 3;
    public static final int MODIFY_ENTRY  = 4;
    public static final int SCHEDULE_ENTRIES = 5;
    private static final String EMPTY_DATE = "";

    private TodoListFile todoListFile = new TodoListFile();
    private UserInput userInput = new UserInput();
    private ListPrinter listPrinter = new ListPrinter();
    private TodoList todoList;

    public CommandHandler() {
        TodoListCalendar todoListCalendar = new TodoListCalendar(listPrinter);
        todoList = new TodoList();
        todoList.setTodoListCalendar(todoListCalendar);
        todoListFile.setTodoList(todoList);
        userInput.setListPrinter(listPrinter);

        tryToLoad();
        handleTodoListCommand();
        tryToSave();
    }

    private void tryToLoad() {
        ArrayList<String> listOfFiles = todoListFile.listFiles();

        if (listOfFiles.size() != 0) {
            String todoListNameForIO = userInput.promptUserForLoad(listOfFiles);
            if (todoListNameForIO != null) {
                todoListFile.load(todoListNameForIO);

                if (todoList.getTodoArray().size() > 0) {
                    System.out.println("Successfully loaded todo-list!");
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to choose action for TodoList: add entry, remove entry, print out list, or
    // quit program and saves it
    // if list is empty, automatically prompts to add an entry
    private void handleTodoListCommand() {
        int choice;

        if (todoList.getTodoListMap().size() == 0) {
            addTodoListEntry();
        }

        do {
            choice = userInput.promptUserForChoiceOfAction();

            switch (choice) {
                case ADD_ENTRY:
                    addTodoListEntry();
                    todoList.sortTodoArray();
                    break;
                case REMOVE_ENTRY:
                    removeEntryIfPossible();
                    break;
                case PRINT_OUT_LIST:
                    printTodoArrayIfPossible();
                    break;
                case MODIFY_ENTRY:
                    modifyEntry();
                    break;
                case SCHEDULE_ENTRIES:
                    scheduleEntries();
                    break;
            }

        } while(choice != QUIT);
    }

    private void printTodoArrayIfPossible() {
        if (todoList.getTodoArray().size() > 0) {
            listPrinter.printEveryTodoArrayEntry(todoList.getTodoArray());
        } else {
            System.out.println("TodoList is empty! Cannot print out anything");
        }
    }

    private void removeEntryIfPossible() {
        if (todoList.getTodoArray().size() == 0) {
            System.out.println("Your lists are empty! Returning to choice menu!");
        } else {
            listPrinter.printEveryTodoArrayEntry(todoList.getTodoArray());

            if (todoList.removeTodoListEntry(userInput.getUserEntryToRemove())) {
                System.out.println("Successfully removed entry!");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Continuously prompts user to add a TodoListEntry until they add a valid entry
    public void addTodoListEntry() {
        String userEntry;
        String date;
        int choice = userInput.getUserEntryType(UserInput.ADD);
        int size = todoList.getTodoArray().size();

        if (choice == UserInput.LEISURE_ENTRY) {
            userEntry = userInput.getLeisureUserEntryToAdd();

             while (!todoList.tryToAddTodoListEntry(userEntry, CommandHandler.EMPTY_DATE)) {
                 System.out.println("Invalid entry! Please try again!\n Note: " +
                         "that entries must have unique names");
                 userEntry = userInput.getLeisureUserEntryToAdd();
             }
        } else {
                userEntry = userInput.getPriorityUserEntryToAdd();
                date = userInput.getUserEntryForDate();

            while (!todoList.tryToAddTodoListEntry(userEntry, date)) {
                System.out.println("Invalid entry! Please try again!\n Note: " +
                        "that entries must have unique names");
                userEntry = userInput.getPriorityUserEntryToAdd();
                date = userInput.getUserEntryForDate();
            }
        }
        printSuccessMessage(size);
    }

    private void printSuccessMessage(int index) {
        TodoListEntry entryAdded = todoList.getTodoArray().get(index);
        System.out.println("Successfully added:\n" +entryAdded.getTodoInfoFormat());
    }


    // EFFECTS: Prompts user for TodoListEntry to modify and modifies the entry chosen
    private void modifyEntry() {
        Map<TodoListEntryActivity, TodoListEntry> todoListEntryMap = todoList.getTodoListMap();

        if (!todoListEntryMap.isEmpty()) {
            TodoListEntry entryToModify = todoListEntryMap.
                    get(userInput.getEntryToModify(todoListEntryMap));

            if (entryToModify instanceof PriorityTodoListEntry) {
                modifyPriorityTodoListEntry((PriorityTodoListEntry) entryToModify);
            } else {
                modifyLeisureTodoListEntry((LeisureTodoListEntry) entryToModify);
            }

        } else {
            System.out.println("No entries to modify!");
        }
    }

    private void modifyLeisureTodoListEntry(LeisureTodoListEntry entryToModify) {
        String revisedEntry;

        do {
            revisedEntry = userInput.getLeisureUserEntryToAdd();
        } while(!entryToModify.modifyEntry(revisedEntry));
    }

    // MODIFIES: entryToModify
    // EFFECTS:
    private void modifyPriorityTodoListEntry(PriorityTodoListEntry entryToModify) {
        String revisedEntry;
        String date;

        do {
            revisedEntry = userInput.getPriorityUserEntryToAdd();
            date = userInput.getUserEntryForDate();
        } while(!entryToModify.modifyEntry(revisedEntry, date));
    }

    private void scheduleEntries() {
        todoList.scheduleEntries();
    }

    // EFFECTS: Prompts user if they want to save TodoList or quit without saving and saves
    // TodoList into file
    private void tryToSave() {
        String todoListNameForIO = userInput.promptUserToSave();
        if (todoListNameForIO != null) {
            todoListFile.save(todoListNameForIO);
        }
    }
}
