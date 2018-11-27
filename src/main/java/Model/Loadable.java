package Model;

import java.io.File;
import java.io.IOException;

public interface Loadable {
    // REQUIRES: valid file with valid state of program
    // MODIFIES: this
    // EFFECTS: loads state of program from file and makes it equal to this
    void load(String fileName);
    void load(File file) throws IOException;
}
