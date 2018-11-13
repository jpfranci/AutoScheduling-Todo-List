package ui;

import Model.TodoListEntryActivity;
import Model.TodoListEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListPrinter {
    // EFFECTS: Prints out TodoList file names
    public void printTodoListFileNames(ArrayList<String> listTodoListNames) {
        for (String todoListName : listTodoListNames ) {
            System.out.println("File name: " +todoListName);
        }
    }

    public void printMap(Map<TodoListEntryActivity, TodoListEntry> todoListEntryHashMap) {
        System.out.println("Your list of activities to do are:");
        for(TodoListEntryActivity activity : todoListEntryHashMap.keySet()) {
            System.out.println(activity.getActivity());
        }

    }

    // EFFECTS: Prints out list and all its contents with index number
    public void printEveryTodoArrayEntry(List<TodoListEntry> todoArray) {
        int index = 0;

        for (TodoListEntry entryGeneral : todoArray) {
            System.out.println("[" + index + "] " +entryGeneral.getTodoInfoFormat());
            index++;
        }
    }
}
