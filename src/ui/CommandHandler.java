package ui;

import Model.*;

import java.util.ArrayList;

public class CommandHandler {
    public static final int PRINT_OUT_LIST = 3;
    public static final int QUIT = 0;
    public static final int ADD_ENTRY = 1;
    public static final int REMOVE_ENTRY = 2;
    public static final String IO_FILE = "todolist.ser";
    public static final String EMPTY_DATE = "";

    TodoListFile todoListFile = new TodoListFile();
    UserInput userInput = new UserInput();
    ListPrinter listPrinter = new ListPrinter();
    TodoList todoList;

    public CommandHandler() {
        todoList = new TodoList();
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

        if (todoList.getTodoArray().size() == 0) {
            addTodoListEntry();
        }

        do {
            choice = userInput.promptUserForChoice();

            switch (choice) {
                case ADD_ENTRY:
                    addTodoListEntry();
                    todoList.sortTodoArray();
                    break;

                case REMOVE_ENTRY:
                    removeEntryIfPossible();
                    break;

                case PRINT_OUT_LIST:
                    if (todoList.getTodoArray().size() > 0) {
                        listPrinter.printEveryEntry(todoList.getTodoArray());
                    }

                    else {
                        System.out.println("TodoList is empty! Cannot print out anything");
                    }
                    break;
            }

        } while(choice != QUIT);
    }

    private void removeEntryIfPossible() {
        if (todoList.getTodoArray().size() == 0) {
            System.out.println("Your lists are empty! Returning to choice menu!");
        }
        else {
            listPrinter.printEveryEntry(todoList.getTodoArray());
            if (todoList.removeTodoListEntry(userInput.getUserEntryToRemove())) {
                System.out.println("Successfully removed entry!");
            }
        }
    }

    // EFFECTS: Prompts user if they want to save TodoList or quit without saving and saves
    // TodoList into file
    private void tryToSave() {
        String todoListNameForIO = userInput.promptUserToSave();
        if (todoListNameForIO != null) {
            todoListFile.save(todoListNameForIO);
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
            do {
                userEntry = userInput.getLeisureUserEntryToAdd();
            } while (!todoList.tryToAddTodoListEntry(userEntry, CommandHandler.EMPTY_DATE));
        }

        else {
            do {
                userEntry = userInput.getPriorityUserEntryToAdd();
                date = userInput.getUserEntryForDate();
            }
            while (!todoList.tryToAddTodoListEntry(userEntry, date));
        }

        TodoListEntry entryAdded = todoList.getTodoArray().get(size);
        if (entryAdded instanceof PriorityTodoListEntry) {
            PriorityTodoListEntry priorityTodoListEntry = (PriorityTodoListEntry) entryAdded;

            System.out.println("Successfully added:\n" + priorityTodoListEntry.getActivity() +
                    ", priority level " + priorityTodoListEntry.getPriorityLevel() +
                    ", which will take " + priorityTodoListEntry.getTime() + " hrs to complete " +
                    "and is due on " + "" + priorityTodoListEntry.getDueDate() + "!");
        }

        else if (entryAdded instanceof LeisureTodoListEntry) {
            LeisureTodoListEntry leisureTodoListEntry = (LeisureTodoListEntry) entryAdded;

            System.out.println("Successfully added:\n" + leisureTodoListEntry.getActivity() +
                    ", which will take " + leisureTodoListEntry.getTime() + " hrs to complete" + "!");
        }

    }

}
