package Test;

import Model.TodoListEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TodoListEntryTest {
    TodoListEntry entry;

    @Test
    public void twoTodoListEntriesAreSame() {
        entry = new TodoListEntry("Basketball", "hIgh", 3);
        TodoListEntry entryTest =  new TodoListEntry("Basketball", "hIgh", 3);

        assertTrue(entry.equals(entryTest));
    }

    @Test
    public void compareNonTodoListEntryWithTodoListEntry() {
        entry = new TodoListEntry("Basketball", "hIgh", 3);
        String s = "Basketball, High, 3.0";

        assertFalse(entry.equals(s));
    }

    @Test
    public void compareTwoTodoListEntriesNotSame() {
        entry = new TodoListEntry("Basketball.", "hIgh", 3);
        TodoListEntry entryTest = new TodoListEntry("Basketball", "hIgh", 3);
        assertFalse(entry.equals(entryTest));
    }

    @Test
    public void compareVoidListWithTodoListEntry() {
        entry = new TodoListEntry("Basketball", "hIgh", 3);
        assertFalse(entry.equals(null));
    }

    @Test
    public void rightCaseProperTodoInfo() {
        entry = new TodoListEntry("Basketball", TodoListEntry.HIGH_STRING, 3);
        assertTrue(entry.getTodoInfo().equals("Basketball, "
                +TodoListEntry.HIGH_STRING+ ", 3.0"));
    }

    @Test
    public void caseInsensitiveProperTodoInfo() {
        entry = new TodoListEntry("Basketball", "hIgh", 3);
        assertTrue(entry.getTodoInfo().equals("Basketball, "
                +TodoListEntry.HIGH_STRING+ ", 3.0"));
    }

    @Test
    public void tryConstructingTodoListEntryCaseInsensitivePriority() {
        entry = new TodoListEntry("Basketball", "hIgh", 3);
        assertTrue(entry.getTodoInfo().equals("Basketball, High, 3.0"));
    }

    @Test
    public void tryConstructingTodoListRightCasePriority() {
        entry = new TodoListEntry("Basketball", TodoListEntry.HIGH_STRING, 3);
        assertTrue(entry.getTodoInfo().equals("Basketball, High, 3.0"));
    }
}
