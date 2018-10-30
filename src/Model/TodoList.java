package Model;

import exceptions.InvalidInputException;


import java.util.ArrayList;
import java.util.Collections;

public class TodoList {
    private ArrayList<TodoListEntry> todoArray = new ArrayList<>();
    private InputChecker inputChecker = new InputChecker();

    // MODIFIES: this
    // EFFECTS: Returns true if String matches format required and adds PriorityTodoListEntry represented by
    // String into TodoList
    public boolean tryToAddTodoListEntry(String userEntry, String date){
       try {
           if (inputChecker.inputFollowsFormat(userEntry))
               todoArray.add(inputChecker.parseTodoListEntry(userEntry, date));

       } catch (InvalidInputException e) {
           System.out.println("Invalid input for activity, priority, time: please try again!");
           return false;
       } catch (NumberFormatException e) {
           System.out.println("Number entered is greater than allowed. Please try again!");
           return false;
       }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Sorts TodoList by descending priority first and if equal priority then by due date
    // and if equal due date then by time needed to complete task
    public void sortTodoArray() {
        Collections.sort(todoArray);
    }

    // MODIFIES: this
    // EFFECTS: Returns true if successfully removed entry at index, returns false otherwise
    public boolean removeTodoListEntry(int indexToRemove) {
        try {
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

    public void setTodoArray(ArrayList<TodoListEntry> todoArray) {
        this.todoArray = todoArray;
    }

    public ArrayList<TodoListEntry> getTodoArray() {
        return todoArray;
    }

}
