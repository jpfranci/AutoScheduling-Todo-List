package Test;

import Model.TodoListEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TodoListEntryTest {
    TodoListEntry entry;

    @Test
    public void twoTodoListEntriesAreSame() {
        entry = new TodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        TodoListEntry entryTest =  new TodoListEntry("Basketball", "hIgh", 3, "2018-12-30");

        assertTrue(entry.equals(entryTest));
    }

    @Test
    public void compareTodoListEntryWithNonTodoListEntry() {
        entry = new TodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        String s = "Basketball, High, 3.0, 2018-12-30";

        assertFalse(entry.equals(s));
    }

    @Test
    public void compareTwoTodoListEntriesActivitiesNotSame() {
        entry = new TodoListEntry("Basketball.", "hIgh", 3, "2018-12-30");
        TodoListEntry entryTest = new TodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Test
    public void compareTwoTodoListEntriesPriorityNotSame() {
        entry = new TodoListEntry("Basketball.", "hIgh", 3, "2018-12-30");
        TodoListEntry entryTest = new TodoListEntry("Basketball", "low", 3, "2018-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Test
    public void compareTwoTodoListEntriesTimeNotSame() {
        entry = new TodoListEntry("Basketball.", "hIgh", 4, "2018-12-30");
        TodoListEntry entryTest = new TodoListEntry("Basketball", "low", 3, "2018-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Test
    public void compareTwoTodoListEntriesDateNotSame() {
        entry = new TodoListEntry("Basketball.", "hIgh", 4, "2018-12-30");
        TodoListEntry entryTest = new TodoListEntry("Basketball", "low", 3, "2019-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Test
    public void compareVoidListWithTodoListEntry() {
        entry = new TodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertFalse(entry.equals(null));
    }

    @Test
    public void rightCaseProperTodoInfo() {
        entry = new TodoListEntry("Basketball", TodoListEntry.HIGH_STRING, 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, "
                +TodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void caseInsensitiveProperTodoInfo() {
        entry = new TodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, "
                +TodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void tryConstructingTodoListEntryCaseInsensitivePriority() {
        entry = new TodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, " +TodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void tryConstructingTodoListRightCasePriority() {
        entry = new TodoListEntry("Basketball", TodoListEntry.HIGH_STRING, 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, " +TodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void testConstructTodoListWithNullDate() {
        entry = new TodoListEntry("Basketball", TodoListEntry.HIGH_STRING, 3, null);
        assertTrue(entry.getTodoInfo().equals("Basketball, " + TodoListEntry.HIGH_STRING + ", 3.0, "
                + LocalDate.now().plusDays(TodoListEntry.DEFAULT_DUE_DATE)));
    }
}
