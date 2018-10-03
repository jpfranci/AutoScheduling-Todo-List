package Model;

public interface Loadable {
    // REQUIRES: valid file with valid state of program
    // MODIFIES: this
    // EFFECTS: loads state of program from file and makes it equal to this
    public void load(String fileName);
}
