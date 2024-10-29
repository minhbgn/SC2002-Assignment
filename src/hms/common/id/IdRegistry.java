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
     * @param clazz The class to get the next ID suffix for.
     * @return The next ID suffix in integer form.
     */
    public static int getNextIdSuffix(Class<? extends IModel> clazz) {
        int id = classIdCounter.get(clazz);
        classIdCounter.put(clazz, id + 1);
        return id;
    }

    /**
     * Tries to update the ID of the class if the provided ID is greater than the current ID.
     * @param clazz The class to update the ID for.
     * @param id The ID to update to.
     */
    public static void tryUpdateId(Class<? extends IModel> clazz, int id) {
        if (id >= classIdCounter.get(clazz)) {
            updateId(clazz, id + 1);
        }
    }

    /**
     * Updates the ID of the class regardless of the current ID.
     * @param clazz The class to update the ID for.
     * @param id The ID to update to.
     */
    public static void updateId(Class<? extends IModel> clazz, int id) {
        classIdCounter.put(clazz, id);
    }
}