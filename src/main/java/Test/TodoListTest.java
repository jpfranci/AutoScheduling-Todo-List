package Test;

import Model.*;
import exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class TodoListTest {
    private TodoList todo;
    private String entry;
    private ArrayList<TodoListEntry> todoArray;
    private String date = "2018-12-30";
    private InputChecker inputChecker = new InputChecker();

    @BeforeEach
    public void setUp() {
        todo = new TodoList();
        todoArray = todo.getTodoArray();
    }

    @Test
    public void addInvalidEntryThreeArgumentsNoSpaces() {
        entry = "Basketball,high,3";
        unsuccessfulMatchTest(entry);
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void tryToAddDuplicateActivityInvalid() {
        entry = "Play Basketball today, high, 3.01";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Play Basketball today", "high", 3.01, 0, date);

        entry = "Play Basketball today, high, 3";
        unsuccessfulAddTest(entry,1, date);
    }

    private void unsuccessfulMatchTest(String entry) {
        try {
            inputChecker.inputFollowsFormat(entry);
            fail("This is supposed to fail");
        } catch(InvalidInputException e) { }
    }

    private void successfulMatchTest (String entry) {
        try {
            inputChecker.inputFollowsFormat(entry);
        } catch(InvalidInputException e) {
            fail("This shouldn't fail");
        }
    }

    @Test
    public void addInvalidEntryThreeArgumentsNoCommas() {
        entry = "Basketball high 3";
        unsuccessfulMatchTest(entry);
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void addInvalidEntryWithSpecialCharacters() {
        entry = "Basketball., high, 3";
        unsuccessfulMatchTest(entry);
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void addMultipleInvalidEntries() {
        entry = "Basketball., high, 3";
        unsuccessfulMatchTest(entry);
        unsuccessfulAddTest(entry,0, date);

        entry = "Basketball high 3";
        unsuccessfulMatchTest(entry);
        unsuccessfulAddTest(entry,0, date);

        entry = "Basketball,high,3";
        unsuccessfulMatchTest(entry);
        unsuccessfulAddTest(entry,0, date);
    }

    @Test
    public void addValidEntryThenInvalidEntryTest() {
        entry = "Play Basketball today, high, 3.01";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Play Basketball today", "high", 3.01, 0, date);

        entry = "Basketball,high,3";
        unsuccessfulMatchTest(entry);
        unsuccessfulAddTest(entry,1, date);
    }

    private void unsuccessfulAddTest(String entry, int index, String date) {
        todo.tryToAddTodoListEntry(entry, date);
        assertTrue(todoArray.size() == index);
    }

    @Test
    public void addValidEntryThreeArgumentsActivityWithSpaces() {
        entry = "Play Basketball today, high, 3";
        date = "2018-12-30";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Play Basketball today", "high", 3, 0, date);
    }

    @Test
    public void addEntryThreeArgumentWithNumber() {
        entry = "Basketball221, high, 3";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Basketball221", "high", 3, 0, date);
    }

    @Test
    public void addEntryCaseInsensitivePriority() {
        entry = "Basketball, HiGh, 3";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Basketball", "HiGh", 3, 0, date);

        entry = "Baskaaadadtball, LoW, 3";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Baskaaadadtball", "LoW", 3, 1, date);

        entry = "Basdadadwfketaball, MEdiUm, 3";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Basdadadwfketaball", "MEdiUm", 3, 2, date);

    }

    @Test
    public void addEntryTimeWithBeginningZeroes() {
        entry = "Basketball, HiGh, 000003";
        successfulMatchTest(entry);
        successfulPriorityAddTest(entry, "Basketball", "HiGh", 000003, 0, date);
    }

    private void successfulPriorityAddTest(String entry, String activity, String priority,
                                           double time, int index, String date) {
        PriorityTodoListEntry testEntry;
        testEntry = new PriorityTodoListEntry(activity, priority, time, date);
        todo.tryToAddTodoListEntry(entry, date);
        assertTrue(testEntry.getTodoInfo().equals(todoArray.get(index).getTodoInfo()));
        assertTrue(todo.getTodoListMap().containsKey(new TodoListEntryActivity(activity, testEntry)));
        assertTrue(todoArray.get(index).getTodoList().equals(todo));
    }

    @Test
    public void addInvalidDateUseSlashes() {
        entry = "Basketball, High, 3";
        invalidDateTest(entry, "2018/09/26", 0);

        entry = "Basketbssall, High, 3";
        invalidDateTest(entry, "2018/09-26", 1);

        entry = "Basketccball, High, 3";
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

        entry = "Basketballcc, High, 3";
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
        todo.tryToAddTodoListEntry(entry, date);
        TodoListEntry todoListEntry = todoArray.get(index);
        PriorityTodoListEntry priorityTodoListEntry = (PriorityTodoListEntry) todoListEntry;

        assertTrue(priorityTodoListEntry.getDueDate().equals
                (LocalDate.now().plusDays(PriorityTodoListEntry.DEFAULT_DUE_DATE)));
    }

    private void validDateTest(String entry, String date, int index) {
        todo.tryToAddTodoListEntry(entry, date);
        TodoListEntry todoListEntry = todoArray.get(index);
        PriorityTodoListEntry priorityTodoListEntry = (PriorityTodoListEntry) todoListEntry;

        assertTrue(priorityTodoListEntry.getDueDate().equals
                (LocalDate.parse(date)));
    }

    @Test
    public void sortListOfOneElement() {
        TodoList t1 = new TodoList();
        addEntriesToTwoLists(t1, "Basketball, LOW, 3.5",
                "Basketball, LOW, 3.5", date, date);

        sortTest(t1);
    }

    @Test
    public void sortOrderedArrayDifferentPrioritiesSameTimeTwoElements() {
        TodoList t1 = new TodoList();


        addEntriesToTwoLists(t1, "Basketballa, HiGh, 3", "Basketballa, HiGh, 3", date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, LOW, 3", date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayDifferentPrioritiesSameTimeTwoElements() {
        TodoList t1 = new TodoList();

        addEntriesToTwoLists(t1, "Basketballa, HiGh, 3", "Basketball, LOW, 3", date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketballa, HiGh, 3", date, date);

        sortTest(t1);
    }

    @Test
    public void sortOrderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
        TodoList t1 = new TodoList();

        addEntriesToTwoLists(t1, "Basketballa, HiGh, 3", "Basketballa, HiGh, 3", date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketball, LOW, 4", date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
        TodoList t1 = new TodoList();

        addEntriesToTwoLists(t1, "Basketballa, HiGh, 3", "Basketball, LOW, 4",date, date);
        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketballa, HiGh, 3",date, date);

        sortTest(t1);
    }

    @Test
    public void sortOrderedArraySamePriorityDifferentTime() {
        TodoList t1 = new TodoList();

        addEntriesToTwoLists(t1, "Basketballa, HiGh, 4", "Basketballa, HiGh, 4" , date, date);
        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3" , date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArraySamePriorityDifferentTime() {
        TodoList t1 = new TodoList();

        addEntriesToTwoLists(t1, "Basketballa, HiGh, 4", "Basketball, HIgh, 3" , date, date);
        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketballa, HIgh, 4" , date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayThreeElementsDifferentPriorityTime() {
        TodoList t1 = new TodoList();

        addEntriesToTwoLists(t1, "Basketballa, HiGh, 4", "Basketballa, HIgh, 4" , date, date);
        addEntriesToTwoLists(t1, "Basketballc, Medium, 3", "Basketball, Medium, 2" , date, date);
        addEntriesToTwoLists(t1, "Basketball, Medium, 2", "Basketballc, Medium, 3" , date, date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArraySamePriorityDifferentTimeDate() {
        TodoList t1 = new TodoList();

        addEntriesToTwoLists(t1, "Basketballa, High, 3", "Basketball, HIgh, 4" ,
                date, "2018-12-31");
        addEntriesToTwoLists(t1, "Basketball, High, 4", "Basketballa, High, 3" ,
                "2018-12-31", date);

        sortTest(t1);
    }

    @Test
    public void sortUnorderedArrayDifferentPriorityDateSameTime() {
        TodoList t1 = new TodoList();


        addEntriesToTwoLists(t1, "Basketballa, High, 3", "Basketball, Medium, 4",
                date, "2018-12-29");

        addEntriesToTwoLists(t1, "Basketball, Medium, 4", "Basketballa, High, 3",
                "2018-12-29", date);

        addEntriesToTwoLists(t1, "Basketballc, 5", "Soccer, 4", date, date);
        addEntriesToTwoLists(t1, "Soccer, 4", "Basketballc, 5", date, date);

        sortTest(t1);
    }

    private void addEntriesToTwoLists (TodoList t1, String toAddToT1, String ToAddToTodo,
                                       String dateT1, String dateTodo) {
        t1.tryToAddTodoListEntry(toAddToT1, dateT1);
        todo.tryToAddTodoListEntry(ToAddToTodo, dateTodo);
    }

    private void sortTest(TodoList t1) {
        todo.sortTodoArray();

        assertTrue(t1.getTodoArray().equals(todo.getTodoArray()));
    }

    @Test
    public void tryToRemoveEntryFromEmptyLists() {
        assertFalse(todo.removeTodoListEntry(1));
        assertFalse(todo.removeTodoListEntry(1));

        assertTrue(todoArray.size() == 0);
        assertTrue(todo.getTodoListMap().isEmpty());
    }

    @Test
    public void removeEntryFromListOneEntry() {
        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
        assertTrue(todo.removeTodoListEntry(0));
        assertTrue(todoArray.size() == 0);
        assertTrue(todo.getTodoListMap().isEmpty());

        todo.tryToAddTodoListEntry("Basketball, 000003", date);
        assertTrue(todo.removeTodoListEntry(0));
        assertTrue(todo.getTodoListMap().isEmpty());
    }

    @Test
    public void removeEntryFromIndexOutOfRangeOneEntry() {
        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
        assertFalse(todo.removeTodoListEntry(1));
        assertTrue(todoArray.size() == 1);
        assertTrue(!todo.getTodoListMap().isEmpty());


        todo.tryToAddTodoListEntry("Basketballa, 000003", date);
        assertFalse(todo.removeTodoListEntry(2));
        assertTrue(todoArray.size() == 2);
        assertTrue(todo.getTodoListMap().size() == 2);

    }

    @Test
    public void removeEntryFromTwoEntries() {

        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);

        todo.tryToAddTodoListEntry("Football, HiGh, 4", date);

        assertTrue(todo.removeTodoListEntry(1));
        assertTrue(todoArray.size() == 1);

        todo.tryToAddTodoListEntry("Baskecctball, 000003", date);
        todo.tryToAddTodoListEntry("Footaaball, 4", date);

        assertTrue(todo.removeTodoListEntry(1));
        assertTrue(todoArray.size() == 2);
    }
}
