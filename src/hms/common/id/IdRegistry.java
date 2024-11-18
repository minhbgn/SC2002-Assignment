package hms.common.id;

import java.util.HashMap;
import hms.common.IModel;

/**
 * Registry for managing IDs of classes.
 * This class is used to manage the IDs of classes that need an ID.
 * It provides methods to get the next ID suffix for a class, update the ID of a class, and try to update the ID of a class.
 */
public class IdRegistry {
    private static final HashMap<Class<? extends IModel>, Integer> classIdCounter = new HashMap<>();

    /**
     * Adds a class to the registry.
     * 
     * @param clazz the class to be added
     * @throws IllegalArgumentException if the class is null or already registered
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