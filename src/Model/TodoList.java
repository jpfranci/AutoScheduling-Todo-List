package Model;

import java.util.ArrayList;
import java.util.Scanner;

public class TodoList {
    private static final int QUIT = 0;
    private static final int ADD_ENTRY = 1;
    private static final int REMOVE_ENTRY = 2;
    private static final int PRINT_OUT_LIST = 3;

    ArrayList<TodoListEntry> todo = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

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

        System.out.println("\nPlease enter the number that you would like to do next\n[" +ADD_ENTRY+ "] add entry \n[" +
                REMOVE_ENTRY+ "] remove an entry \n[" +PRINT_OUT_LIST+ "] print " + "out your list " +
                "\n[" +QUIT+ "] quit");
        do {
            choice = getInt();
        } while(choice < 0 || choice > 3); // checks for valid input

        return choice;
    }


    private void promptUserForTodoListEntry() {
        int choice;

        do {
            System.out.println("\nPlease enter a todo-list entry as follows:\nActivity, priority " +
                    "(low, medium, or high), time needed (in hrs)");
            System.out.println("An example would be: Play Basketball, medium, 4");

            getUserEntry();

            System.out.println("Press:\n[any number]: to continue adding\n" +
                    "[0]: to stop adding entries, \n");
            choice = getInt();

        } while(choice != 0);
    }

    private int getInt() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to add Entries to Todo-List and checks if valid input
    // If valid input, then adds to todo-list and prints out a success message
    private void getUserEntry() {
        String userEntry;

        do{
            userEntry = scanner.nextLine();
        } while(!userEntry.matches("(\\w *){1,}, (?i)(high|medium|low){1}, \\d{1,}"));

        String userEntries[] = userEntry.split(", ");

        int time = Integer.parseInt(userEntries[2]);

        TodoListEntry todoListEntry = new TodoListEntry(userEntries[0], userEntries[1], time);
        todo.add(todoListEntry);
        System.out.println("Successfully added:\n" + userEntries[0] + ", priority level "
                + userEntries[1]  + ", which will take " + time + " hrs to complete!\n");
    }

    private void promptUserToRemoveEntry() {
        printTodoList();
        System.out.println("\nPlease enter the activity name you would like to remove from the list\n");

        String entryToRemove = scanner.nextLine();

        int index = 0;

        for (TodoListEntry entry : todo) {
            if (entry.getActivity().equalsIgnoreCase(entryToRemove)) {
                todo.remove(index);
                System.out.println("\nSuccessfully removed " +entryToRemove+ " from todo-list!");
                return;
            }
            index++;
        }
        System.out.println("\nError: " +entryToRemove+ " was not in the todo-list");
    }

    // prints out contents of TodoList
    public void printTodoList() {
        System.out.println("\nYou have to:");

        if (todo.size() == 0) {
            System.out.println("Your todo-list is empty");
        }

        else {

            for (TodoListEntry entry : todo) {
                String activity = entry.getActivity();
                String priorityLevel = entry.getPriorityLevel();
                double time = entry.getTime();

                System.out.println(activity + " which is " + priorityLevel + " priority and will take "
                        + time + " hours");
            }
        }
    }


}
