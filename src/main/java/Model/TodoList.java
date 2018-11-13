package Model;

import exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.*;

public class TodoList implements Serializable {
    private ArrayList<TodoListEntry> todoArray = new ArrayList<>();
    private Map<TodoListEntryActivity, TodoListEntry> todoListMap = new TreeMap<>();
    private InputChecker inputChecker = new InputChecker();
    private TodoListCalendar todoListCalendar;

    // MODIFIES: this
    // EFFECTS: Initializes todoListMap based on the values of todoListEntries
    public void initializeMap(ArrayList<TodoListEntry> todoListEntries) {
        for (TodoListEntry entry : todoListEntries) {
            todoListMap.put(entry.todoListEntryActivity, entry);
        }
    }

    // MODIFIES: this
    // EFFECTS: Returns true if String matches format required and adds PriorityTodoListEntry represented by
    // String into TodoList
    public boolean tryToAddTodoListEntry(String userEntry, String date){
       try {
           if (inputChecker.inputFollowsFormat(userEntry)) {
               TodoListEntry newEntry = inputChecker.parseTodoListEntry(userEntry, date);

               if(!isNotInMap(newEntry)) {
                   todoArray.add(newEntry);
                   todoListMap.put(newEntry.getTodoListEntryActivity(), newEntry);
                   newEntry.setTodoList(this);
               }
               else {
                   return false;
               }
           }

       } catch (InvalidInputException e) {
           System.out.println("Invalid input for todoListEntryActivity, priority, time: please try again!");
           return false;
       } catch (NumberFormatException e) {
           System.out.println("Number entered is greater than allowed. Please try again!");
           return false;
       }
        return true;
    }

    public boolean isNotInMap(TodoListEntry newEntry) {
        return todoListMap.containsKey(newEntry.getTodoListEntryActivity());
    }

    // MODIFIES: this
    // EFFECTS: Returns true if successfully removed entry at index, returns false otherwise
    public boolean removeTodoListEntry(int indexToRemove) {
        try {
            TodoListEntry entryToRemove = todoArray.get(indexToRemove);
            todoListMap.remove(entryToRemove.getTodoListEntryActivity());
            entryToRemove.removeTodoList();
            todoArray.remove(indexToRemove);
        }
        catch(IndexOutOfBoundsException e) {
            System.out.println("Error: Invalid user entry number!");
            return false;
        }
        finally {
            System.out.println("Going back to choice menu!");
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Sorts TodoList by descending priority first and if equal priority then by due date
    // and if equal due date then by time needed to complete task
    public void sortTodoArray() {
        Collections.sort(todoArray);
    }

    public void addToTodoArray(TodoListEntry entry) {
        if (!todoArray.contains(entry)) {
            todoArray.add(entry);
            entry.setTodoList(this);
        }
    }

    public void scheduleEntries() {
        todoListCalendar.tryToScheduleEntries(todoListMap);
    }

    // MODIFIES: this
    // EFFECTS: Sets this to todoArray
    public void setTodoArray(ArrayList<TodoListEntry> todoArray) {
        this.todoArray = todoArray;
    }

    public void setTodoListMap(Map<TodoListEntryActivity, TodoListEntry> todoListHashMap) {
        this.todoListMap = todoListHashMap;
    }

    public void setTodoListCalendar(TodoListCalendar todoListCalendar) {
        this.todoListCalendar = todoListCalendar;
    }

    public ArrayList<TodoListEntry> getTodoArray() {
        return todoArray;
    }

    public Map<TodoListEntryActivity, TodoListEntry> getTodoListMap() {
        return todoListMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoList)) return false;
        TodoList todoList = (TodoList) o;
        return todoArray.equals(todoList.todoArray) &&
                todoListMap.equals(todoList.todoListMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoArray, todoListMap);
    }
}
