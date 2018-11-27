package Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;


public class TodoListFile implements Loadable, Saveable{
    public static final String FILE_EXTENSION = ".json";
    public static final String DIRECTORY = "Saved";
    private File currentDir = new File(DIRECTORY);
    private TodoList todoList;

    // EFFECTS: Parses directory for TodoList files (.ser) and returns a list with their file names
    public ArrayList<String> listFiles() {
        ArrayList<String> todoListFileNames = new ArrayList<>();

        if (!currentDir.exists()) {
            createDirectory();
        } else {
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
    private void createDirectory() {
        currentDir.mkdir();
    }

    @Override
    // REQUIRES: Valid file containing first only serialized ArrayList<PriorityTodoListEntry>
    // and second ArrayList<LeisureTodoListEntry>
    // MODIFIES: this
    // EFFECTS: loads file and generates list from its contents, prints out its contents if not empty
    public void load(String fileName) {
        try {
            loadGeneric(new File(DIRECTORY
                    + File.separator + fileName));
        } catch (IOException e) {
            System.out.println("File could not be successfully loaded");
        }
    }


    @Override
    public void load(File file) throws IOException{
        loadGeneric(file);
    }

    private void loadGeneric(File fileName) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<TodoListEntry>> typeReference =
                    new TypeReference<List<TodoListEntry>>() {};
            ArrayList<TodoListEntry> todoListEntries = objectMapper.readValue(fileName, typeReference);

            todoList.initializeTodoList(todoListEntries);

    }


    @Override
    // EFFECTS: Saves state of TodoList in a file
    public void save(String fileName) {
        try {
            saveGeneric(DIRECTORY
                    + File.separator + fileName);
        }  catch (FileNotFoundException e) {
            System.out.println("Error: File could not be found!");
        } catch (IOException e) {
            System.out.println("Error: File could not be created or found!");
        }
    }

    private void saveGeneric(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ArrayList<TodoListEntry>> typeReference =
                new TypeReference<ArrayList<TodoListEntry>>() {
                };
        String json = mapper.writerFor(typeReference).withDefaultPrettyPrinter()
                .writeValueAsString(todoList.getTodoArray());

        FileWriter writer = new FileWriter(fileName);
        writer.write(json);
        writer.close();

        System.out.println("Successfully saved " + fileName + "!");
    }

    @Override
    public void save(File file) throws IOException {
        saveGeneric(file.getAbsolutePath());
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }
}
