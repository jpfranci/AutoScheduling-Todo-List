package Test;

import Model.TodoList;
import Model.TodoListFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.CommandHandler;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoListFileTest {
    TodoList todo;
    TodoListFile todoListFile;
    private String date = "2018-12-30";

    @BeforeEach
    public void setup() {
        todo = new TodoList();
        todoListFile = new TodoListFile();
        todoListFile.setTodoList(todo);
    }

    @Test
    public void saveTodoListEmpty() {
        todoListFile.save(CommandHandler.IO_FILE);
        loadFile(0);
    }

    @Test
    public void saveTodoListOneEntry() {
        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
        todoListFile.save(CommandHandler.IO_FILE);
        loadFile(1);
    }

    @Test
    public void saveTodoListMultipleEntries() {
        todo.tryToAddTodoListEntry("Basketball, HiGh, 000003", date);
        todo.tryToAddTodoListEntry("Football, HiGh, 4", date);
        todo.tryToAddTodoListEntry("Pokemon, high, 3", date);
        todo.tryToAddTodoListEntry("Pokemona, 4", null);

        todoListFile.save(CommandHandler.IO_FILE);
        loadFile(4);
    }

    private void loadFile(int numSaved) {
        TodoList t1 = new TodoList();
        TodoListFile t2 = new TodoListFile();
        t2.setTodoList(t1);

        t2.load(CommandHandler.IO_FILE);
        assertTrue(t1.getTodoArray().size() == numSaved);
        assertTrue(t1.getTodoListMap().size() == numSaved);
    }

}
