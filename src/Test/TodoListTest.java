package Test;

import Model.TodoList;
import Model.TodoListEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TodoListTest {
    public static final int sizeStringList = 3;

    private TodoList todo;
    private String entry;
    private ArrayList<TodoListEntry> todoList;
    private TodoListEntry testEntry;

    @BeforeEach
    public void setUp() {
        todo = new TodoList(2);
        todoList = todo.getTodoArray();
    }

    @Test
    public void addInvalidEntryThreeArgumentsNoSpaces() {
        entry = "Basketball,high,3";
        unsuccessfulAddTest(entry,0);
    }

    @Test
    public void addInvalidEntryThreeArgumentsNoCommas() {
        entry = "Basketball high 3";
        unsuccessfulAddTest(entry,0);
    }

    @Test
    public void addInvalidEntryWithSpecialCharacters() {
        entry = "Basketball., high, 3";
        unsuccessfulAddTest(entry,0);
    }

    @Test
    public void addMultipleInvalidEntries() {
        entry = "Basketball., high, 3";
        unsuccessfulAddTest(entry,0);

        entry = "Basketball high 3";
        unsuccessfulAddTest(entry,0);

        entry = "Basketball,high,3";
        unsuccessfulAddTest(entry,0);
    }

    @Test
    public void addValidEntryThenInvalidEntryTest() {
        entry = "Play Basketball today, high, 3";
        successfulAddTest(entry, "Play Basketball today", "high", 3, 0);

        entry = "Basketball,high,3";
        unsuccessfulAddTest(entry,1);
    }

    private void unsuccessfulAddTest(String entry, int index) {
        todo.addTodoListEntry(entry);
        assertTrue(todoList.size() == index);
    }

    @Test
    public void addValidEntryThreeArgumentsActivityWithSpaces() {
        entry = "Play Basketball today, high, 3";
        successfulAddTest(entry, "Play Basketball today", "high", 3, 0);
    }

    @Test
    public void addEntryThreeArgumentWithNumber() {
        entry = "Basketball221, high, 3";
        successfulAddTest(entry, "Basketball221", "high", 3, 0);
    }

    @Test
    public void addEntryCaseInsensitivePriority() {
        entry = "Basketball, HiGh, 3";
        successfulAddTest(entry, "Basketball", "HiGh", 3, 0);

        entry = "Basketball, LoW, 3";
        successfulAddTest(entry, "Basketball", "LoW", 3, 1);

        entry = "Basketball, MEdiUm, 3";
        successfulAddTest(entry, "Basketball", "MEdiUm", 3, 2);

    }

    @Test
    public void addEntryTimeWithBeginningZeroes() {
        entry = "Basketball, HiGh, 000003";
        successfulAddTest(entry, "Basketball", "HiGh", 000003, 0);
    }

    private void successfulAddTest(String entry,String activity, String priority, double time, int index) {
        testEntry = new TodoListEntry(activity, priority, time);
        todo.addTodoListEntry(entry);
        assertTrue(testEntry.getTodoInfo().equals(todoList.get(index).getTodoInfo()));
    }

    @Test
    public void parseStringCorrectly() {
        String[] parseListTest = {"Basketball221", "high", "3"};
        String[] parseList = todo.parseString("Basketball221, high, 3");

        for (int i = 0; i < sizeStringList; i++) {
            assertTrue(parseListTest[i].equals(parseList[i]));
        }
    }

    @Test
    public void sortListofOneElement() {
        TodoList t1 = new TodoList(2);
        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, LOW, 3");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }


    @Test
    public void sortOrderedArrayDifferentPrioritiesSameTimeTwoElements() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3");
        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, LOW, 3");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void sortUnorderedArrayDifferentPrioritiesSameTimeTwoElements() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, LOW, 3");
        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, HiGh, 3");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void sortOrderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3");
        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketball, LOW, 4");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void sortUnorderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, LOW, 4");
        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketball, HiGh, 3");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void sortOrderedArraySamePriorityDifferentTime() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HiGh, 4");
        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void sortUnorderedArraySamePriorityDifferentTime() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HIgh, 3");
        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HIgh, 4");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void sortUnorderedArrayThreeElementsDifferentPriorityTime() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HIgh, 4");
        addEntriesToTwoLists(t1, "Basketball, Medium, 3", "Basketball, Medium, 2");
        addEntriesToTwoLists(t1, "Basketball, Medium, 2", "Basketball, Medium, 3");

        todo.sortTodoListByDescendingPriorityAndTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    private void addEntriesToTwoLists (TodoList t1, String toAddToT1, String ToAddToTodo) {
        t1.addTodoListEntry(toAddToT1);
        todo.addTodoListEntry(ToAddToTodo);
    }

    @Test
    public void tryToRemoveEntryFromEmptyList() {
        assertFalse(todo.removeTodoListEntry(1));
        assertTrue(todoList.size() == 0);
    }

    @Test
    public void removeEntryFromListOneEntry() {
        todo.addTodoListEntry("Basketball, HiGh, 000003");
        assertTrue(todo.removeTodoListEntry(0));
        assertTrue(todoList.size() == 0);
    }

    @Test
    public void removeEntryFromIndexOutOfRangeOneEntry() {
        todo.addTodoListEntry("Basketball, HiGh, 000003");
        assertFalse(todo.removeTodoListEntry(1));
        assertTrue(todoList.size() == 1);
    }

    @Test
    public void removeEntryFromTwoEntries() {
        todo.addTodoListEntry("Basketball, HiGh, 000003");
        todo.addTodoListEntry("Football, HiGh, 4");

        assertTrue(todo.removeTodoListEntry(1));
        assertTrue(todoList.size() == 1);
    }

}
