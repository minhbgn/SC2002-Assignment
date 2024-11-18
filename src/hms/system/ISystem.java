package hms.system;

/**
 * Interface for a system that can be run.
 * 
 * All systems must implement this interface.
 * 
 * A system handles displaying the UI, processing user input, and updating the state of the system.
 * 
 * Systems can be run in a chain, with each system returning the next system to run. In case of a null return, the system chain is terminated. A system can be run multiple times, but it must return the same next system to run each time.
 */
public interface ISystem {
    /**
     * Run the system
     * @return the next system to run, or null if the system is done
     */
    public ISystem run();
}
