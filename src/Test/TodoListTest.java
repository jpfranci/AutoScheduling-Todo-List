//package Test;
//
//import Model.LeisureTodoListEntry;
//import Model.PriorityTodoListEntry;
//import Model.TodoList;
//import exceptions.InvalidInputException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ui.CommandHandler;
//import ui.UserInput;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.fail;
//
//public class TodoListTest {
//    private TodoList todo;
//    private String entry;
//    private ArrayList<PriorityTodoListEntry> priorityTodoList;
//    private ArrayList<LeisureTodoListEntry> leisureTodoList;
//    private String date = "2018-12-30";
//
//    @BeforeEach
//    public void setUp() {
//        todo = new TodoList();
//        priorityTodoList = todo.getPriorityTodoArray();
//        leisureTodoList = todo.getLeisureTodoArray();
//    }
//
//    @Test
//    public void addInvalidEntryThreeArgumentsNoSpaces() {
//        entry = "Basketball,high,3";
//        unsuccessfulMatchTest(entry);
//        unsuccessfulAddTest(entry,0, date);
//    }
//
//    private void unsuccessfulMatchTest(String entry) {
//        try {
//            todo.inputFollowsFormat(entry);
//            fail("This is supposed to fail");
//        } catch(InvalidInputException e) { }
//    }
//
//    private void successfulMatchTest (String entry) {
//        try {
//            todo.inputFollowsFormat(entry);
//        } catch(InvalidInputException e) {
//            fail("This shouldn't fail");
//        }
//    }
//
//    @Test
//    public void addInvalidEntryThreeArgumentsNoCommas() {
//        entry = "Basketball high 3";
//        unsuccessfulMatchTest(entry);
//        unsuccessfulAddTest(entry,0, date);
//    }
//
//    @Test
//    public void addInvalidEntryWithSpecialCharacters() {
//        entry = "Basketball., high, 3";
//        unsuccessfulMatchTest(entry);
//        unsuccessfulAddTest(entry,0, date);
//    }
//
//    @Test
//    public void addMultipleInvalidEntries() {
//        entry = "Basketball., high, 3";
//        unsuccessfulMatchTest(entry);
//        unsuccessfulAddTest(entry,0, date);
//
//        entry = "Basketball high 3";
//        unsuccessfulMatchTest(entry);
//        unsuccessfulAddTest(entry,0, date);
//
//        entry = "Basketball,high,3";
//        unsuccessfulMatchTest(entry);
//        unsuccessfulAddTest(entry,0, date);
//    }
//
//    @Test
//    public void addValidEntryThenInvalidEntryTest() {
//        entry = "Play Basketball today, high, 3.01";
//        successfulMatchTest(entry);
//        successfulAddTest(entry, "Play Basketball today", "high", 3.01, 0, date);
//
//        entry = "Basketball,high,3";
//        unsuccessfulMatchTest(entry);
//        unsuccessfulAddTest(entry,1, date);
//    }
//
//    private void unsuccessfulAddTest(String entry, int index, String date) {
//        todo.tryToAddTodoListEntry(entry, date);
//        assertTrue(priorityTodoList.size() == index);
//    }
//
//    @Test
//    public void addValidEntryThreeArgumentsActivityWithSpaces() {
//        entry = "Play Basketball today, high, 3";
//        date = "2018-12-30";
//        successfulMatchTest(entry);
//        successfulAddTest(entry, "Play Basketball today", "high", 3, 0, date);
//    }
//
//    @Test
//    public void addEntryThreeArgumentWithNumber() {
//        entry = "Basketball221, high, 3";
//        successfulMatchTest(entry);
//        successfulAddTest(entry, "Basketball221", "high", 3, 0, date);
//    }
//
//    @Test
//    public void addEntryCaseInsensitivePriority() {
//        entry = "Basketball, HiGh, 3";
//        successfulMatchTest(entry);
//        successfulAddTest(entry, "Basketball", "HiGh", 3, 0, date);
//
//        entry = "Basketball, LoW, 3";
//        successfulMatchTest(entry);
//        successfulAddTest(entry, "Basketball", "LoW", 3, 1, date);
//
//        entry = "Basketball, MEdiUm, 3";
//        successfulMatchTest(entry);
//        successfulAddTest(entry, "Basketball", "MEdiUm", 3, 2, date);
//
//    }
//
//    @Test
//    public void addEntryTimeWithBeginningZeroes() {
//        entry = "Basketball, HiGh, 000003";
//        successfulMatchTest(entry);
//        successfulAddTest(entry, "Basketball", "HiGh", 000003, 0, date);
//    }
//
//    private void successfulAddTest(String entry, String activity, String priority,
//                                   double time, int index, String date) {
//        PriorityTodoListEntry testEntry;
//        testEntry = new PriorityTodoListEntry(activity, priority, time, date);
//        todo.tryToAddTodoListEntry(entry, date);
//        assertTrue(testEntry.getTodoInfo().equals(priorityTodoList.get(index).getTodoInfo()));
//    }
//
//    @Test
//    public void addInvalidDateUseSlashes() {
//        entry = "Basketball, High, 3";
//        invalidDateTest(entry, "2018/09/26", 0);
//
//        entry = "Basketball, High, 3";
//        invalidDateTest(entry, "2018/09-26", 1);
//
//        entry = "Basketball, High, 3";
//        invalidDateTest(entry, "2018-09/26", 2);
//    }
//
//    @Test
//    public void addInvalidDateInvalidMonth() {
//        entry = "Basketball, High, 3";
//        invalidDateTest(entry, "2018-13-26", 0);
//    }
//
//    @Test
//    public void addValidMonthStartingWithZeroAndStartingWithOne() {
//        entry = "Basketball, High, 3";
//        validDateTest(entry, "2018-09-26", 0);
//
//        entry = "Basketball, High, 3";
//        validDateTest(entry, "2018-12-26", 1);
//    }
//
//    @Test
//    public void addInvalidDateDayIsInvalid() {
//        entry = "Basketball, High, 3";
//        invalidDateTest(entry, "2018/09/32", 0);
//    }
//
//    @Test
//    public void addInvalidDateYearIsInvalid() {
//        entry = "Basketball, High, 3";
//        invalidDateTest(entry, "1018/09/31", 0);
//    }
//
//    private void invalidDateTest(String entry, String date, int index) {
//        todo.tryToAddTodoListEntry(entry, date);
//        assertTrue(priorityTodoList.get(index).getDueDate().equals
//                (LocalDate.now().plusDays(PriorityTodoListEntry.DEFAULT_DUE_DATE)));
//    }
//
//    private void validDateTest(String entry, String date, int index) {
//        todo.tryToAddTodoListEntry(entry, date);
//        assertTrue(priorityTodoList.get(index).getDueDate().equals
//                (LocalDate.parse(date)));
//    }
//
//
//    @Test
//    public void sortListOfOneElement() {
//        TodoList t1 = new TodoList();
//        addEntriesToTwoLists(t1, "Basketball, LOW, 3.5",
//                "Basketball, LOW, 3.5", date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortOrderedArrayDifferentPrioritiesSameTimeTwoElements() {
//        TodoList t1 = new TodoList();
//
//
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3", date, date);
//        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, LOW, 3", date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortUnorderedArrayDifferentPrioritiesSameTimeTwoElements() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, LOW, 3", date, date);
//        addEntriesToTwoLists(t1, "Basketball, LOW, 3", "Basketball, HiGh, 3", date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortOrderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
//        TodoList t1 = new TodoList();
//
//
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3", date, date);
//        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketball, LOW, 4", date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortUnorderedArrayDifferentPrioritiesDifferentTimeTwoElements() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, LOW, 4",date, date);
//        addEntriesToTwoLists(t1, "Basketball, LOW, 4", "Basketball, HiGh, 3",date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortOrderedArraySamePriorityDifferentTime() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HiGh, 4" , date, date);
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HiGh, 3" , date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortUnorderedArraySamePriorityDifferentTime() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HIgh, 3" , date, date);
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 3", "Basketball, HIgh, 4" , date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortUnorderedArrayThreeElementsDifferentPriorityTime() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, HiGh, 4", "Basketball, HIgh, 4" , date, date);
//        addEntriesToTwoLists(t1, "Basketball, Medium, 3", "Basketball, Medium, 2" , date, date);
//        addEntriesToTwoLists(t1, "Basketball, Medium, 2", "Basketball, Medium, 3" , date, date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortUnorderedArraySamePriorityDifferentTimeDate() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, High, 3", "Basketball, HIgh, 4" ,
//                date, "2018-12-31");
//        addEntriesToTwoLists(t1, "Basketball, High, 4", "Basketball, High, 3" ,
//                "2018-12-31", date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortUnorderedArrayDifferentPriorityDateSameTime() {
//        TodoList t1 = new TodoList();
//
//
//        addEntriesToTwoLists(t1, "Basketball, High, 3", "Basketball, Medium, 4",
//                date, "2018-12-29");
//
//        addEntriesToTwoLists(t1, "Basketball, Medium, 4", "Basketball, High, 3",
//                "2018-12-29", date);
//
//        sortTestPriority(t1);
//    }
//
//    @Test
//    public void sortUnorderedLeisureArraySameTimeOneElement() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, 3", "Basketball, 3", date, date);
//
//        sortTestLeisure(t1);
//    }
//
//    @Test
//    public void sortUnorderedLeisureArraySameTimeTwoElements() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, 3", "Basketball, 3", date, date);
//        addEntriesToTwoLists(t1, "Soccer, 3", "Soccer, 3", date, date);
//
//        sortTestLeisure(t1);
//    }
//
//    @Test
//    public void sortUnorderedLeisureArrayDifferentTimesThreeElements() {
//        TodoList t1 = new TodoList();
//
//        addEntriesToTwoLists(t1, "Basketball, 5", "Soccer, 4", date, date);
//        addEntriesToTwoLists(t1, "Soccer, 4", "Basketball, 5", date, date);
//        addEntriesToTwoLists(t1, "Baseball, 4", "Baseball, 4", date, date);
//
//
//        sortTestLeisure(t1);
//    }
//
//    private void sortTestLeisure(TodoList t1) {
//        todo.sortLeisureTodoList();
//        assertTrue(t1.getLeisureTodoArray().equals(todo.getLeisureTodoArray()));
//    }
//
//
//    private void addEntriesToTwoLists (TodoList t1, String toAddToT1, String ToAddToTodo,
//                                       String dateT1, String dateTodo) {
//        t1.tryToAddTodoListEntry(toAddToT1, dateT1);
//        todo.tryToAddTodoListEntry(ToAddToTodo, dateTodo);
//    }
//
//    private void sortTestPriority(TodoList t1) {
//        todo.sortTodoArray();
//
//        assertTrue(t1.getPriorityTodoArray().equals(todo.getPriorityTodoArray()));
//    }
//
//    @Test
//    public void tryToRemoveEntryFromEmptyLists() {
//        assertFalse(todo.removeTodoListEntry(1, UserInput.PRIORITY_ENTRY));
//        assertFalse(todo.removeTodoListEntry(1, UserInput.PRIORITY_ENTRY));
//
//        assertTrue(priorityTodoList.size() == 0);
//        assertTrue(leisureTodoList.size() == 0);
//    }
//
//    @Test
//    public void removeEntryFromListOneEntry() {
//        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
//        assertTrue(todo.removeTodoListEntry(0, UserInput.PRIORITY_ENTRY));
//        assertTrue(priorityTodoList.size() == 0);
//
//        todo.tryToAddTodoListEntry("Basketball, 000003", date);
//        assertTrue(todo.removeTodoListEntry(0, UserInput.LEISURE_ENTRY));
//        assertTrue(leisureTodoList.size() == 0);
//    }
//
//    @Test
//    public void removeEntryFromIndexOutOfRangeOneEntry() {
//        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
//        assertFalse(todo.removeTodoListEntry(1, UserInput.PRIORITY_ENTRY));
//        assertTrue(priorityTodoList.size() == 1);
//
//        todo.tryToAddTodoListEntry("Basketball, 000003", date);
//        assertFalse(todo.removeTodoListEntry(1, UserInput.LEISURE_ENTRY));
//        assertTrue(leisureTodoList.size() == 1);
//    }
//
//    @Test
//    public void removeEntryFromTwoEntries() {
//
//        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
//
//        todo.tryToAddTodoListEntry("Football, HiGh, 4", date);
//
//        assertTrue(todo.removeTodoListEntry(1, UserInput.PRIORITY_ENTRY));
//        assertTrue(priorityTodoList.size() == 1);
//
//        todo.tryToAddTodoListEntry("Basketball, 000003", date);
//        todo.tryToAddTodoListEntry("Football, 4", date);
//
//        assertTrue(todo.removeTodoListEntry(1, UserInput.LEISURE_ENTRY));
//        assertTrue(priorityTodoList.size() == 1);
//    }
//
//    @Test
//    public void saveTodoListEmpty() {
//        todo.save(TodoList.IO_FILE);
//
//        loadFile(0,0);
//    }
//
//    @Test
//    public void saveTodoListOneEntry() {
//        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
//        todo.save(CommandHandler.IO_FILE);
//        loadFile(1,0);
//    }
//
//    @Test
//    public void saveTodoListMultipleEntries() {
//        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
//        todo.tryToAddTodoListEntry("Football, HiGh, 4", date);
//        todo.tryToAddTodoListEntry("Pokemon, high, 3", date);
//        todo.tryToAddTodoListEntry("Pokemon, 4", null);
//
//        todo.save(CommandHandler.IO_FILE);
//        loadFile(3, 1);
//
//    }
//
//    private void loadFile(int numSavedPriority, int numSavedLeisure) {
//        TodoList t1 = new TodoList();
//        t1.load(CommandHandler.IO_FILE);
//        assertTrue(t1.getPriorityTodoArray().size() == numSavedPriority &&
//        t1.getLeisureTodoArray().size() == numSavedLeisure);
//    }
//
//}
