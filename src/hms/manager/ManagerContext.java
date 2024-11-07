package hms.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * Context class for storing and managing the managers.
 */
public class ManagerContext {
    // Map to store the managers
    private final Map<Class<?>, IManager> managers = new HashMap<>();

    /**
     * Adds a manager to the context.
     * @param managerClass The class of the manager.
     * @param manager The manager instance.
     */
    public void addManager(Class<?> managerClass, IManager manager) {
        manager.initialize(); // Initialize the manager if it hasn't been initialized yet
        managers.put(managerClass, manager);
    }

    /**
     * Retrieves a manager from the context.
     * @param managerClass The class of the manager.
     * @param <T> The type of the manager.
     * @return The manager instance.
     */
    @SuppressWarnings("unchecked")
    public <T extends IManager> T getManager(Class<T> managerClass) {
        return (T) managers.get(managerClass);
    }

    /**
     * Saves all the managers. This method should be called before exiting the application.
     */
    public void save() {
        managers.values().forEach(IManager::save);
    }
}
