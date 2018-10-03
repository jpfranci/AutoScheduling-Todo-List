package Model;


public interface Saveable {

    // EFFECTS: Saves state of program into a file
    public void save(String fileName);
}
