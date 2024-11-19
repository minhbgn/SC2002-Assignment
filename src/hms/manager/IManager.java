package hms.manager;

/**
 * Interface for the Manager classes,
 * which are responsible for interacting their respective repositories.
 * All Manager classes should implement this interface.
 * <p>
 * They should include ways to:
 * - Initializing the repository with data from a file (through the load() method)
 * - Saving the repository data to a file when the program exits (through the save() method)
 * - Providing methods to interact models stored in the repository
 * - Validating input data across other managers before interacting with the repository
 * <p>
 * The initialize() method should be called before any other methods in the Manager class.
 * It handle any necessary setup for the manager such as loading data from a file.
 * It should also be idempotent, meaning that calling it multiple times
 * should not have any side effects.
 */
public interface IManager {
    /** Initializes the manager. */
    public void initialize();
    
    /** Loads the data from the file. */
    public void load();

    /** Saves the data to the file. */
    public void save();
}
