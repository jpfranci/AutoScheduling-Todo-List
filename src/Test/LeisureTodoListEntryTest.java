package Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import Model.LeisureTodoListEntry;
import org.junit.jupiter.api.Test;

public class LeisureTodoListEntryTest extends TodoListEntryTest{

    @Override
    @Test
    public void twoTodoListEntriesAreSame() {
        entry = new LeisureTodoListEntry("Basketball",3);
        LeisureTodoListEntry entryTest =  new LeisureTodoListEntry("Basketball",3);

        assertTrue(entry.equals(entryTest));
    }

    @Override
    @Test
    public void compareTodoListEntryWithNonTodoListEntry() {
        entry = new LeisureTodoListEntry("Basketball", 3);
        String s = "Basketball, 3.0";

        assertFalse(entry.equals(s));
    }

    @Override
    @Test
    public void compareTwoTodoListEntriesActivitiesNotSame() {
        entry = new LeisureTodoListEntry("Basketball.", 3);
        LeisureTodoListEntry entryTest = new LeisureTodoListEntry("Basketball",  3);
        assertFalse(entry.equals(entryTest));
    }

    @Override
    @Test
    public void compareTwoTodoListEntriesTimeNotSame() {
        entry = new LeisureTodoListEntry("Basketball", 4);
        LeisureTodoListEntry entryTest = new LeisureTodoListEntry("Basketball",3);
        assertFalse(entry.equals(entryTest));
    }

    @Override
    @Test
    public void compareVoidListWithTodoListEntry() {
        entry = new LeisureTodoListEntry("Basketball", 4);
        assertFalse(entry.equals(null));
    }

    @Override
    @Test
    public void rightCaseProperTodoInfo() {
        entry = new LeisureTodoListEntry("Basketball", 4);
        assertTrue(entry.getTodoInfo().equals("Basketball, 4.0"));
    }
}
