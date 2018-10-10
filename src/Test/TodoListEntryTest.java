package Test;

import Model.TodoListEntry;
import org.junit.jupiter.api.Test;

public abstract class TodoListEntryTest {
    TodoListEntry entry;

    @Test
    public abstract void twoTodoListEntriesAreSame();

    @Test
    public abstract void compareTodoListEntryWithNonTodoListEntry();

    @Test
    public abstract void compareTwoTodoListEntriesActivitiesNotSame();

    @Test
    public abstract void compareTwoTodoListEntriesTimeNotSame();

    @Test
    public abstract void compareVoidListWithTodoListEntry();

    @Test
    public abstract void rightCaseProperTodoInfo();
}
