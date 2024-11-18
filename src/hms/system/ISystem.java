package hms.system;

/**
 * Interface for a system, which handles retrieving user input
 * to execute the available options. All systems must implement this interface.
 * <p>
 * Note: Systems can be run in a chain, with each system returning the next system to run.
 * In case of a null return, the system chain is terminated.
 * A system can be run repeatedly by returning itself.
 * <p>
 * Acts as a boundary class, called the system layer.
 */
public interface ISystem {
    /**
     * Run the system
     * @return the next system to run, or null if the system is done
     */
    public ISystem run();
}
