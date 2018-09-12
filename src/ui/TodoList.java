package ui;

import java.util.ArrayList;

public class TodoList {
    ArrayList<TodoListEntry> todo = new ArrayList<>();

    // constructs a TodoList
    public TodoList() {
        TodoListEntry entry1 = new TodoListEntry("Do CPSC 210 Pre-Lab", 5, 2.5);
        TodoListEntry entry2 = new TodoListEntry("Play soccer", 0, 1);

        addItems(entry1, entry2);
        printTodoList(todo);
    }

    // adds two Items into TodoList
    public void addItems(TodoListEntry e1, TodoListEntry e2) {
        todo.add(e1);
        todo.add(e2);
        System.out.println("Successfully added entries into todo-list!");
    }

    // prints out contents of TodoList
    public void printTodoList(ArrayList<TodoListEntry> todo) {
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
