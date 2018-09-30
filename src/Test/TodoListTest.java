package Test;

import Model.TodoList;
import Model.TodoListEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TodoListTest {
    private TodoList todo;
    private String entry;
    private ArrayList<TodoListEntry> todoList;
    private String date = "2018-12-30";

    @BeforeEach
    public void setUp() {
        todo = new TodoList(2);
        todoList = todo.getTodoArray();
    }

    @Test
    public void addInvalidEntryThreeArgumentsNoSpaces() {
        entry = "Basketball,high,3";
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void addInvalidEntryThreeArgumentsNoCommas() {
        entry = "Basketball high 3";
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void addInvalidEntryWithSpecialCharacters() {
        entry = "Basketball., high, 3";
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void addMultipleInvalidEntries() {
        entry = "Basketball., high, 3";
        unsuccessfulAddTest(entry,0, date);

        entry = "Basketball high 3";
        unsuccessfulAddTest(entry,0, date);

        entry = "Basketball,high,3";
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void addValidEntryThenInvalidEntryTest() {
        entry = "Play Basketball today, high, 3";
        successfulAddTest(entry, "Play Basketball today", "high", 3, 0, date);

        entry = "Basketball,high,3";
        unsuccessfulAddTest(entry,1, date);
    }

    private void unsuccessfulAddTest(String entry, int index, String date) {
        todo.addTodoListEntry(entry, date);
        assertTrue(todoList.size() == index);
    }

    @Test
    public void addValidEntryThreeArgumentsActivityWithSpaces() {
        entry = "Play Basketball today, high, 3";
        date = "2018-12-30";
        successfulAddTest(entry, "Play Basketball today", "high", 3, 0, date);
    }

    @Test
    public void addEntryThreeArgumentWithNumber() {
        entry = "Basketball221, high, 3";
        successfulAddTest(entry, "Basketball221", "high", 3, 0, date);
    }

    @Test
    public void addEntryCaseInsensitivePriority() {
        entry = "Basketball, HiGh, 3";
        successfulAddTest(entry, "Basketball", "HiGh", 3, 0, date);

        entry = "Basketball, LoW, 3";
        successfulAddTest(entry, "Basketball", "LoW", 3, 1, date);

        entry = "Basketball, MEdiUm, 3";
        successfulAddTest(entry, "Basketball", "MEdiUm", 3, 2, date);

    }

    @Test
    public void addEntryTimeWithBeginningZeroes() {
        entry = "Basketball, HiGh, 000003";
        successfulAddTest(entry, "Basketball", "HiGh", 000003, 0, date);
    }

    private void successfulAddTest(String entry, String activity, String priority,
                                   double time, int index, String date) {
        TodoListEntry testEntry;
        testEntry = new TodoListEntry(activity, priority, time, date);
        todo.addTodoListEntry(entry, date);
        assertTrue(testEntry.getTodoInfo().equals(todoList.get(index).getTodoInfo()));
    }

    @Test
    public void addInvalidDateUseSlashes() {
        entry = "Basketball, High, 3";
        invalidDateTest(entry, "2018/09/26", 0);

        entry = "Basketball, High, 3";
        invalidDateTest(entry, "2018/09-26", 1);

        entry = "Basketball, High, 3";
        invalidDateTest(entry, "2018-09/26", 2);
    }

    @Test
    public void addInvalidDateInvalidMonth() {
        entry = "Basketball, High, 3";
        invalidDateTest(entry, "2018-13-26", 0);
    }

    @Test
    public void addValidMonthStartingWithZeroAndStartingWithOne() {
        entry = "Basketball, High, 3";
        validDateTest(entry, "2018-09-26", 0);

        entry = "Basketball, High, 3";
        validDateTest(entry, "2018-12-26", 1);
    }

    @Test
    public void addInvalidDateDayIsInvalid() {
        entry = "Basketball, High, 3";
        invalidDateTest(entry, "2018/09/32", 0);
    }

    @Test
    public void addInvalidDateYearIsInvalid() {
        entry = "Basketball, High, 3";
        invalidDateTest(entry, "1018/09/31", 0);
    }

    private void invalidDateTest(String entry, String date, int index) {
        todo.addTodoListEntry(entry, date);
        assertTrue(todoList.get(index).getDueDate().equals
                (LocalDate.now().plusDays(TodoListEntry.DEFAULT_DUE_DATE)));
    }

    private void validDateTest(String entry, String date, int index) {
        todo.addTodoListEntry(entry, date);
        assertTrue(todoList.get(index).getDueDate().equals
                (LocalDate.parse(date)));
    }


    @Test
    public void sortListOfOneElement() {
        TodoList t1 = new TodoList(2);
        addEntriesToTwoLists(t1, "Basketball, LOW, 3",
                "Basketball, LOW, 3", date, date);

        sortTest(t1);
    }

    @Test
    public void sortOrderedArrayDifferentPrioritiesSameTimeTwoElements() {
        TodoList t1 = new TodoList(2);


        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3" , date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, LOW, 3", date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayDifferentPrioritiesSameTimeTwoElements() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, LOW, 3", date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, HiGh, 3", date, date);

        sortTest(t1);
    }

    @Test
    public void sortOrderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3", date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketball, LOW, 4", date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, LOW, 4",date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketball, HiGh, 3",date, date);

        sortTest(t1);
    }

    @Test
    public void sortOrderedArraySamePriorityDifferentTime() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HiGh, 4" , date, date);
        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3" , date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArraySamePriorityDifferentTime() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HIgh, 3" , date, date);
        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HIgh, 4" , date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayThreeElementsDifferentPriorityTime() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HIgh, 4" , date, date);
        addEntriesToTwoLists(t1, "Basketball, Medium, 3", "Basketball, Medium, 2" , date, date);
        addEntriesToTwoLists(t1, "Basketball, Medium, 2", "Basketball, Medium, 3" , date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArraySamePriorityDifferentTimeDate() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, High, 3", "Basketball, HIgh, 4" ,
                date, "2018-12-31");
        addEntriesToTwoLists(t1, "Basketball, High, 4", "Basketball, High, 3" ,
                "2018-12-31", date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayDifferentPriorityDateSameTime() {
        TodoList t1 = new TodoList(2);

        addEntriesToTwoLists(t1, "Basketball, High, 3", "Basketball, Medium, 4" ,
                date, "2018-12-29");
        addEntriesToTwoLists(t1, "Basketball, Medium, 4", "Basketball, High, 3" ,
                "2018-12-29", date);

        sortTest(t1);
    }


    private void addEntriesToTwoLists (TodoList t1, String toAddToT1, String ToAddToTodo,
                                       String dateT1, String dateTodo) {
        t1.addTodoListEntry(toAddToT1, dateT1);
        todo.addTodoListEntry(ToAddToTodo, dateTodo);
    }

    private void sortTest(TodoList t1) {
        todo.sortTodoListByPriorityThenDateThenTime();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void tryToRemoveEntryFromEmptyList() {
        assertFalse(todo.removeTodoListEntry(1));
        assertTrue(todoList.size() == 0);
    }

    @Test
    public void removeEntryFromListOneEntry() {
        todo.addTodoListEntry("Basketball, HiGh, 000003", date);
        assertTrue(todo.removeTodoListEntry(0));
        assertTrue(todoList.size() == 0);
    }

    @Test
    public void removeEntryFromIndexOutOfRangeOneEntry() {
        todo.addTodoListEntry("Basketball, HiGh, 000003", date);
        assertFalse(todo.removeTodoListEntry(1));
        assertTrue(todoList.size() == 1);
    }

    @Test
    public void removeEntryFromTwoEntries() {
        todo.addTodoListEntry("Basketball, HiGh, 000003", date);
        todo.addTodoListEntry("Football, HiGh, 4", date);

        assertTrue(todo.removeTodoListEntry(1));
        assertTrue(todoList.size() == 1);
    }

}
