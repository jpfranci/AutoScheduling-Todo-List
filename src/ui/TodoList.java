package ui;

import java.util.ArrayList;
import java.util.Scanner;

public class TodoList {
    private static final int QUIT = 0;
    private static final int ADD_ENTRY = 1;
    private static final int REMOVE_ENTRY = 2;
    private static final int PRINT_OUT_LIST = 3;

    ArrayList<TodoListEntry> todo = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    // constructs a TodoList
    public TodoList() {
        promptUserForAction();
    }

    private void promptUserForAction() {
        int choice;
        if (todo.size() == 0) {
            promptUserForTodoListEntry();
        }

        do {
            choice = promptUserForChoice();

            switch (choice) {
                case ADD_ENTRY:
                    promptUserForTodoListEntry();
                    break;
                case REMOVE_ENTRY:
                    promptUserToRemoveEntry();
                    break;
                case PRINT_OUT_LIST:
                    printTodoList();
                    break;
            }

        } while(choice != QUIT);
    }

    private int promptUserForChoice() {
        int choice;

        System.out.println("\nPlease enter " +ADD_ENTRY+ " if you would like to add another entry to the list, \n" +
                REMOVE_ENTRY+ " if you would like to remove an entry, \n" +PRINT_OUT_LIST+ " " +
                "if you would like to print " + "out your list, \nand " +QUIT+ " if you would like to quit");
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(choice < 0 || choice > 3); // checks for valid input

        return choice;
    }

    private void promptUserForTodoListEntry() {
        int choice;
        do {
            System.out.println("\nPlease enter a todo-list entry as follows: Activity, priority " +
                    "(low, medium, or high), time needed (in hrs)");
            System.out.println("An example would be: Play Basketball, medium, 4");

            getUserEntry(todo.size());

            System.out.println("Press 0 if you would like to stop adding entries, " +
                    "or any number to continue adding entries.\n");
            choice = scanner.nextInt();
            scanner.nextLine();

        } while(choice != 0);
    }

    private void getUserEntry(int size) {
        String userEntry = scanner.nextLine();
        String userEntries[] = userEntry.split(", ");

        int time = Integer.parseInt(userEntries[2]);

        TodoListEntry todoListEntry = new TodoListEntry(userEntries[0], userEntries[1], time);
        todo.add(todoListEntry);
        System.out.println("Successfully added:\n" + userEntries[0] + ", priority level "
                + userEntries[1] + ", which will take " + time + " hrs to complete!\n");
    }

    private void promptUserToRemoveEntry() {
        System.out.println("Your list of activities are: ");
        printTodoList();
        System.out.println("Please enter the activity name you would like to remove from the list");

    }

    // prints out contents of TodoList
    public void printTodoList() {
        int size = todo.size();

        System.out.println("\nYou have to:");

        for(int i = 0; i < size; i++) {
            TodoListEntry entry = todo.get(i);
            String activity = entry.getActivity();
            String priorityLevel = entry.getPriorityLevel();
            double time = entry.getTime();

            System.out.println(activity+ " which is a " +priorityLevel+ " priority activity and will take "
                    +time+ " hours to complete");
        }
    }

    public static void main(String[] args) {
        new TodoList();
    }
}
