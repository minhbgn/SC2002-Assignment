package hms.manager;

/**
 * Interface for the Manager classes.
 */
public interface IManager {
    /**
     * Initializes the manager.
     */
    public void initialize();
    
    /**
     * Loads the data from the file.
     */
    public void load();

    /**
     * Saves the data to the file.
     */
    public void save();
}
