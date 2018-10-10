package Model;

import java.io.File;
import java.lang.String;
import java.util.ArrayList;

public class TodoListFile {
    public static final String FILE_EXTENSION = ".ser";
    public static final String DIRECTORY = "Saved";
    private File currentDir = new File(DIRECTORY);

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


}
