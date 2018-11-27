package Model;


import java.io.File;
import java.io.IOException;

public interface Saveable {
    // EFFECTS: Saves state of program into a file with name
    void save (String fileName);
    void save(File file) throws IOException;
}
