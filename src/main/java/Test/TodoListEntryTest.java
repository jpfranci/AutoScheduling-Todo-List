package Test;

import org.junit.jupiter.api.Test;

public abstract class TodoListEntryTest {
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

    @Test
    public abstract void modifyEntryValid();

    @Test
    public abstract void modifyEntryInvalid();
}
