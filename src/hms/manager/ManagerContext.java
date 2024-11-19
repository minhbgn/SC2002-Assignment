package hms.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * The manager context is a class that stores all the managers in the application. It provides a way to add and retrieve managers.
 * The context is used to ensure that there is only one instance of each manager in the application.
 */
public class ManagerContext {
    /** The map of managers. */
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

    /** Saves all the managers. This method should be called when the application is closing. */
    public void save() {
        managers.values().forEach(IManager::save);
    }
}
