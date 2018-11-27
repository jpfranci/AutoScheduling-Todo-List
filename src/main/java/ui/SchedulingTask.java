package ui;


import Model.TodoList;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

public class SchedulingTask extends Task<TodoList> {
    private final TodoList todoList;

    public SchedulingTask(TodoList todoList) {
        this.todoList = todoList;
    }

    @Override
    protected TodoList call() throws Exception {
        todoList.scheduleEntries();
        return null;
    }

}
