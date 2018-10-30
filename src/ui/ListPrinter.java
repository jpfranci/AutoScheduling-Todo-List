package ui;

import Model.LeisureTodoListEntry;
import Model.PriorityTodoListEntry;
import Model.TodoListEntry;

import java.util.ArrayList;
import java.util.List;

public class ListPrinter {
    // EFFECTS: Prints out TodoList file names
    void printTodoListFileNames(ArrayList<String> listTodoListNames) {
        for (String todoListName : listTodoListNames ) {
            System.out.println("File name: " +todoListName);
        }
    }

    // EFFECTS: Prints out list and all its contents with index number
    void printEveryEntry(List<TodoListEntry> todoArray) {
        int index = 0;

        for (TodoListEntry entryGeneral : todoArray) {
            if (entryGeneral instanceof PriorityTodoListEntry) {
                PriorityTodoListEntry entry = (PriorityTodoListEntry) entryGeneral;
                System.out.println("[" + index + "] " + entry.getActivity() + " which is "
                        + entry.getPriorityLevel() + " priority and will take "
                        + entry.getTime() + " hours and is due on " + entry.getDueDate());
            }

            else if (entryGeneral instanceof LeisureTodoListEntry) {
                LeisureTodoListEntry entry = (LeisureTodoListEntry) entryGeneral;
                System.out.println("[" + index + "] " + entry.getActivity() + " which will take "
                        + entry.getTime() + " hours.");
            }
            index++;

        }
    }
}
