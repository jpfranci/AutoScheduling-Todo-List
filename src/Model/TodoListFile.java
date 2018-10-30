package Model;

import java.io.*;
import java.lang.String;
import java.util.ArrayList;

public class TodoListFile implements Loadable, Saveable{
    public static final String FILE_EXTENSION = ".ser";
    public static final String DIRECTORY = "Saved";
    private File currentDir = new File(DIRECTORY);
    private TodoList todoList;

    // EFFECTS: Parses directory for TodoList files (.ser) and returns a list with their file names
    public ArrayList<String> listFiles() {
        ArrayList<String> todoListFileNames = new ArrayList<String>();

        if (!currentDir.exists()) {
            createDir();
        }

        else {
            File[] todoListFiles = currentDir.listFiles((dir, fileName) -> fileName.endsWith(FILE_EXTENSION));

            if (todoListFiles != null) {
                for (File file : todoListFiles) {
                    todoListFileNames.add(file.toString().substring(DIRECTORY.length() + 1));
                    // substring removes file directory from name
                }
            }
        }

        return todoListFileNames;
    }

    // MODIFIES: this
    // EFFECTS: Creates a directory in system
    private void createDir() {
        currentDir.mkdir();
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    @Override
    // REQUIRES: Valid file containing first only serialized ArrayList<PriorityTodoListEntry>
    // and second ArrayList<LeisureTodoListEntry>
    // MODIFIES: this
    // EFFECTS: loads file and generates list from its contents, prints out its contents if not empty
    public void load(String fileName) {
        try {
            FileInputStream inFile = new FileInputStream(TodoListFile.DIRECTORY
                    + File.separator + fileName);
            ObjectInputStream inputStream = new ObjectInputStream(inFile);

            todoList.setTodoArray((ArrayList<TodoListEntry>) inputStream.readObject());

            inputStream.close();
            inFile.close();
        }
        catch (IOException e) {
            System.out.println("Error: loading file! Creating new Todo-List");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Todo-list not found in file! Creating new Todo-List");
        }
    }

    @Override
    // EFFECTS: Saves state of TodoList in a file
    public void save(String fileName) {
        try {
            FileOutputStream outFile = new FileOutputStream(TodoListFile.DIRECTORY
                    + File.separator + fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(outFile);

            outputStream.writeObject(todoList.getTodoArray());

            outputStream.close();
            outFile.close();
            System.out.println("Successfully saved todo-list to " +TodoListFile.DIRECTORY
                    + File.separator + fileName+ "!");
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: File could not be found!");
        }
        catch (IOException e) {
            System.out.println("Error: File could not be created or found!");
        }
    }
}
