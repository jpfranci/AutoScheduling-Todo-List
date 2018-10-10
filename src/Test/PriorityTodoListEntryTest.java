package Test;

import Model.PriorityTodoListEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PriorityTodoListEntryTest extends TodoListEntryTest {

    @Override
    @Test
    public void twoTodoListEntriesAreSame() {
        entry = new PriorityTodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        PriorityTodoListEntry entryTest =  new PriorityTodoListEntry("Basketball", "hIgh", 3, "2018-12-30");

        assertTrue(entry.equals(entryTest));
    }

    @Override
    @Test
    public void compareTodoListEntryWithNonTodoListEntry() {
        entry = new PriorityTodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        String s = "Basketball, High, 3.0, 2018-12-30";

        assertFalse(entry.equals(s));
    }

    @Override
    @Test
    public void compareTwoTodoListEntriesActivitiesNotSame() {
        entry = new PriorityTodoListEntry("Basketball.", "hIgh", 3, "2018-12-30");
        PriorityTodoListEntry entryTest = new PriorityTodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Test
    public void compareTwoTodoListEntriesPriorityNotSame() {
        entry = new PriorityTodoListEntry("Basketball.", "hIgh", 3, "2018-12-30");
        PriorityTodoListEntry entryTest = new PriorityTodoListEntry("Basketball", "low", 3, "2018-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Override
    @Test
    public void compareTwoTodoListEntriesTimeNotSame() {
        entry = new PriorityTodoListEntry("Basketball.", "hIgh", 4, "2018-12-30");
        PriorityTodoListEntry entryTest = new PriorityTodoListEntry("Basketball", "low", 3, "2018-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Test
    public void compareTwoTodoListEntriesDateNotSame() {
        entry = new PriorityTodoListEntry("Basketball.", "hIgh", 4, "2018-12-30");
        PriorityTodoListEntry entryTest = new PriorityTodoListEntry("Basketball", "low", 3, "2019-12-30");
        assertFalse(entry.equals(entryTest));
    }

    @Override
    @Test
    public void compareVoidListWithTodoListEntry() {
        entry = new PriorityTodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertFalse(entry.equals(null));
    }

    @Test
    @Override
    public void rightCaseProperTodoInfo() {
        entry = new PriorityTodoListEntry("Basketball", PriorityTodoListEntry.HIGH_STRING, 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, "
                + PriorityTodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void caseInsensitiveProperTodoInfo() {
        entry = new PriorityTodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, "
                + PriorityTodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void tryConstructingTodoListEntryCaseInsensitivePriority() {
        entry = new PriorityTodoListEntry("Basketball", "hIgh", 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, " + PriorityTodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void tryConstructingTodoListRightCasePriority() {
        entry = new PriorityTodoListEntry("Basketball", PriorityTodoListEntry.HIGH_STRING, 3, "2018-12-30");
        assertTrue(entry.getTodoInfo().equals("Basketball, " + PriorityTodoListEntry.HIGH_STRING+ ", 3.0, 2018-12-30"));
    }

    @Test
    public void testConstructTodoListWithNullDate() {
        entry = new PriorityTodoListEntry("Basketball", PriorityTodoListEntry.HIGH_STRING, 3, null);
        assertTrue(entry.getTodoInfo().equals("Basketball, " + PriorityTodoListEntry.HIGH_STRING + ", 3.0, "
                + LocalDate.now().plusDays(PriorityTodoListEntry.DEFAULT_DUE_DATE)));
    }
}
