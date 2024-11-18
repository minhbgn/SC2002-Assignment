package hms.common.id;

import hms.common.IModel;
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
    private static final HashMap<Class<? extends IModel>, Integer> classIdCounter = new HashMap<>();

    /** Private constructor to prevent instantiation. */
    private IdRegistry() { }

    /**
     * Adds a class to the registry. This method is package-private
     * and should only be called by the IdManager.
     * @param clazz The class to add to the registry.
     */
    static void addClass(Class<? extends IModel> clazz) {        
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        if (classIdCounter.containsKey(clazz)) {
            throw new IllegalArgumentException("Class already registered");
        }

        classIdCounter.put(clazz, 0);
    }

    /**
     * Gets the next ID suffix for the provided class.
     * 
     * @param clazz the class to get the next ID suffix for
     * @return the next ID suffix in integer form
     * @throws IllegalArgumentException if the class is not registered
     */
    static int getNextIdSuffix(Class<? extends IModel> clazz) {
        if (!classIdCounter.containsKey(clazz)) {
            throw new IllegalArgumentException("Class not registered");
        }
        return classIdCounter.get(clazz) + 1;
    }

    /**
     * Updates the ID of the provided class.
     * 
     * @param clazz the class to update the ID for
     * @param idSuffix the new ID suffix
     * @throws IllegalArgumentException if the class is not registered or the ID suffix is not greater than the current suffix
     */
    static void updateId(Class<? extends IModel> clazz, int idSuffix) {
        if (!classIdCounter.containsKey(clazz)) {
            throw new IllegalArgumentException("Class not registered");
        }

        if (idSuffix <= classIdCounter.get(clazz)) {
            throw new IllegalArgumentException("ID suffix must be greater than the current suffix");
        }

        classIdCounter.put(clazz, idSuffix);
    }

    /**
     * Tries to update the ID of the provided class.
     * 
     * @param clazz the class to update the ID for
     * @param idSuffix the new ID suffix
     * @return true if the ID was updated, false otherwise
     */
    static boolean tryUpdateId(Class<? extends IModel> clazz, int idSuffix) {
        if (!classIdCounter.containsKey(clazz)) {
            return false;
        }

        if (idSuffix <= classIdCounter.get(clazz)) {
            return false;
        }

        classIdCounter.put(clazz, idSuffix);
        return true;
    }
}