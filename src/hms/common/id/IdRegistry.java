package hms.common.id;

import java.util.HashMap;

/**
 * Registry for Ids of registered classes.
 * This class is used to manage the next available Id of registered classes.
 * <p>
 * This is to ensure that each class has a unique Id suffix when generating Ids, 
 * therefore making an id unique across all classes in combination with the class name prefix.
 * @see IdParser IdParser for managing the class name prefix.
 */
public class IdRegistry {
    /** The map of classes to their next available ID suffix. */
    private static final HashMap<Class<?>, Integer> classIdCounter = new HashMap<>();

    /** Private constructor to prevent instantiation. */
    private IdRegistry() { }

    /**
     * Adds a class to the registry. This method is package-private
     * and should only be called by the IdManager.
     * @param clazz The class to add to the registry.
     */
    static void addClass(Class<?> clazz) {        
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        if (classIdCounter.containsKey(clazz)) {
            throw new IllegalArgumentException("Class already registered");
        }

        classIdCounter.put(clazz, 1);
    }

    /**
     * Gets the next ID suffix for the provided class.
     * @param clazz The class to get the next ID suffix for.
     * @return The next ID suffix in integer form.
     */
    public static int getNextIdSuffix(Class<?> clazz) {
        int id = classIdCounter.get(clazz);
        classIdCounter.put(clazz, id + 1);
        return id;
    }

    /**
     * Tries to update the ID of the class if the provided ID is greater than the current ID.
     * @param clazz The class to update the ID for.
     * @param id The ID to update to.
     */
    public static void tryUpdateId(Class<?> clazz, int id) {
        if (id >= classIdCounter.get(clazz)) {
            updateId(clazz, id + 1);
        }
    }

    /**
     * Updates the ID of the class regardless of the current ID.
     * @param clazz The class to update the ID for.
     * @param id The ID to update to.
     */
    public static void updateId(Class<?> clazz, int id) {
        classIdCounter.put(clazz, id);
    }
}