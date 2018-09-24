package ui;

import Model.TodoList;

import java.util.Scanner;

public class UserInput {
    private Scanner scanner = new Scanner(System.in);

    // EFFECTS: Prompts user to choose between adding entry, removing entry, and quitting entry and returns
    // the user's choice
    public int promptUserForChoice() {
        int choice;

        System.out.println("\nPlease enter the number that you would like to do next\n[" +TodoList.ADD_ENTRY+ "] add entry \n[" +
                TodoList.REMOVE_ENTRY+ "] remove an entry \n[" +TodoList.PRINT_OUT_LIST+ "] print " + "out your list " +
                "\n[" +TodoList.QUIT+ "] quit");
        do {
            choice = scanInt();
        } while(choice < 0 || choice > 3); // checks for valid input

        return choice;
    }

    // EFFECTS: Prompts user for entry to add to list and returns the string that the user entered
    public String getUserEntryToAdd() {
        System.out.println("\nPlease enter a todo-list entry as follows:\nActivity, priority " +
                "(low, medium, or high), time needed (in hrs)");
        System.out.println("An example would be: Play Basketball, medium, 4");

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
