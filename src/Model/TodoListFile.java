package Model;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.String;
import java.util.ArrayList;

public class TodoListFile {
    public static final String FILE_EXTENSION = ".ser";
    public static final String DIRECTORY = ".";
    private File currentDir = new File(DIRECTORY);


    // EFFECTS: Parses directory for TodoList files (.ser) and returns a list with their file names
    public ArrayList<String> listFiles() {
        ArrayList<String> todoListFileNames = new ArrayList<String>();
        File[] todoListFiles = currentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String fileName) {
                return fileName.endsWith(FILE_EXTENSION);
            }
        });

        if (todoListFiles != null) {
            for (File file : todoListFiles) {
                todoListFileNames.add(file.toString().substring(2)); // substring removes file directory from name
            }
        }

        return todoListFileNames;
    }


}
