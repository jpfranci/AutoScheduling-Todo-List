package Model;

import exceptions.AlreadyInTodoListException;
import exceptions.InvalidActivityException;
import exceptions.InvalidInputException;
import exceptions.InvalidTimeException;

import java.io.Serializable;
import java.util.*;

public class TodoList implements Serializable {
    private ArrayList<TodoListEntry> todoArray = new ArrayList<>();
    private Map<TodoListEntryActivity, TodoListEntry> todoListMap = new TreeMap<>();
    private Set<String> activities = new HashSet<>();
    private InputChecker inputChecker = new InputChecker();
    private TodoListCalendar todoListCalendar;

    // MODIFIES: this
    // EFFECTS: Initializes todoListMap based on the values of todoListEntries
    public void initializeTodoList(ArrayList<TodoListEntry> todoListEntries) {
        setTodoArray(todoListEntries);
        todoListMap.clear();

        for (TodoListEntry entry : todoListEntries) {
            TodoListEntryActivity todoListEntryActivity = entry.getTodoListEntryActivity();
            todoListMap.put(todoListEntryActivity, entry);
            activities.add(todoListEntryActivity.getActivity());
        }
    }

    // MODIFIES: this
    // EFFECTS: Returns true if String matches format required and adds PriorityTodoListEntry represented by
    // String into TodoList
    public boolean tryToAddTodoListEntry(String userEntry, String date) {
       try {
           if (inputChecker.inputFollowsFormat(userEntry)) {
               TodoListEntry newEntry = inputChecker.parseTodoListEntry(userEntry, date);
               TodoListEntryActivity todoListEntryActivity = newEntry.getTodoListEntryActivity();
               String activity = todoListEntryActivity.getActivity();

               if(!isInTodoList(activity)) {
                   todoArray.add(newEntry);
                   todoListMap.put(todoListEntryActivity, newEntry);
                   activities.add(activity);
                   newEntry.setTodoList(this);
               } else {
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

    public boolean isInTodoList(String activity) {
        return activities.contains(activity);
    }

    public boolean isActivityInTodoList(String activity) {
        return todoListMap.containsKey(new TodoListEntryActivity(activity, null));
    }

    // MODIFIES: this
    // EFFECTS: Returns true if successfully removed entry at index, returns false otherwise
    public boolean removeTodoListEntry(int indexToRemove) {
        try {
            TodoListEntry entryToRemove = todoArray.get(indexToRemove);
            activities.remove(entryToRemove.getTodoListEntryActivity().getActivity());
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

    public void removeTodoListEntries(List<TodoListEntry> todoListEntries) {
        ArrayList<TodoListEntry> todoListEntriesCopy = new ArrayList<>(todoListEntries);
        todoArray.removeAll(todoListEntries);

        List<TodoListEntryActivity> todoListEntryActivities = new ArrayList<>();
        List<String> todoListStrings = new ArrayList<>();

        for (TodoListEntry todoListEntry : todoListEntriesCopy) {
            TodoListEntryActivity todoListEntryActivity = todoListEntry.getTodoListEntryActivity();
            todoListEntryActivities.add(todoListEntryActivity);
            todoListStrings.add(todoListEntryActivity.getActivity());
        }
        todoListMap.keySet().removeAll(todoListEntryActivities);
        activities.removeAll(todoListStrings);
    }

    // MODIFIES: this
    // EFFECTS: Adds a TodoListEntry to TodoList if valid input, else throws one of the subtypes(time, alreadyInTodoList,
    // priority) exception
    public TodoListEntry addTodoListEntry(String activity, String dueDate, String time, String priority)
            throws InvalidActivityException, InvalidTimeException, AlreadyInTodoListException {
        TodoListEntry todoListEntry;
        TodoListEntryActivity todoListEntryActivity;
        double timeDouble;

        if (isNotModified(activity)) {
            throw new InvalidActivityException();
        }
        timeDouble = getTimeAsDouble(time);

        if (isNotModified(dueDate) && isNotModified(priority)) {
            todoListEntry = new LeisureTodoListEntry(activity, timeDouble);
        } else {
            todoListEntry = new PriorityTodoListEntry(activity, priority, timeDouble, dueDate);
        }

        todoListEntryActivity = todoListEntry.getTodoListEntryActivity();

        if (isInTodoList(todoListEntryActivity.getActivity())) {
            throw new AlreadyInTodoListException();
        }
        todoArray.add(todoListEntry);
        todoListMap.put(todoListEntryActivity, todoListEntry);
        activities.add(todoListEntryActivity.getActivity());
        return todoListEntry;
    }

    private double getTimeAsDouble(String time) throws InvalidTimeException {
        double timeDouble;
        try {
            timeDouble = Double.parseDouble(time);
        } catch (NumberFormatException e) {
            throw new InvalidTimeException();
        }
        return timeDouble;
    }
    private boolean isNotModified(String type) {
        return type.length() == 0;
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
    private void setTodoArray(ArrayList<TodoListEntry> todoArray) {
        this.todoArray = todoArray;
    }

    public void createNewTodoList() {
        todoArray.clear();
        todoListMap.clear();
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
