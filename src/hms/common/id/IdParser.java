package hms.common.id;

import hms.common.IModel;
import java.util.HashMap;

/**
 * This class gets the prefix and suffix id for different classes.
 * This ensures no id of different class objects can be clashing with each other.
 */
public class IdParser {
    private static final HashMap<Class<? extends IModel>, String> classToIdPrefix = new HashMap<>();

    /**
     * Adds a class and its corresponding prefix to the parser.
     * 
     * @param clazz the class to be added
     * @param prefix the prefix to be associated with the class
     * @throws IllegalArgumentException if the class is null, the prefix is not exactly 2 characters long,
     *                                  the prefix is not uppercase letters only, the prefix is already in use,
     *                                  or the class is already registered
     */
    static void addClass(Class<? extends IModel> clazz, String prefix) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        if (prefix.length() != 2) {
            throw new IllegalArgumentException("Prefix must be exactly 2 characters long");
        }

        if (!prefix.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Prefix must be uppercase letters only");
        }

        if (classToIdPrefix.containsValue(prefix)) {
            throw new IllegalArgumentException("Prefix already in use");
        }

        if (classToIdPrefix.containsKey(clazz)) {
            throw new IllegalArgumentException("Class already registered");
        }

        classToIdPrefix.put(clazz, prefix);
    }

    /**
     * Gets the prefix associated with a class.
     * 
     * @param clazz the class whose prefix is to be retrieved
     * @return the prefix associated with the class
     * @throws IllegalArgumentException if the class is not registered
     */
    static String getPrefix(Class<? extends IModel> clazz) {
        if (!classToIdPrefix.containsKey(clazz)) {
            throw new IllegalArgumentException("Class not registered");
        }
        return classToIdPrefix.get(clazz);
    }

    /**
     * Get the prefix for the given class.
     * 
     * @param clazz The class to get the prefix for.
     * @return The prefix for the given class.
     */
    public static String getIdPrefix(Class<? extends IModel> clazz) {
        String prefix = classToIdPrefix.get(clazz);
        if (prefix == null) {
            throw new IllegalArgumentException("Class not supported");
        }
        return prefix;
    }

    /**
     * Get the prefix for the given ID by extracting the first two characters.
     * 
     * @param id The ID to get the prefix for.
     * @return The prefix for the given ID.
     */
    public static String getIdPrefix(String id) throws IllegalArgumentException {
        if(id.length() < 6) {
            throw new IllegalArgumentException("Invalid ID: ID too short");
        }

        return id.substring(0, 2);
    }

    /**
     * Get the suffix for the given ID by parsing from the third character onwards as an integer.
     * 
     * @param id The ID to get the suffix for.
     * @return The suffix for the given ID in integer form.
     */
    public static int getIdSuffix(String id) throws IllegalArgumentException {
        if(id.length() < 6) {
            throw new IllegalArgumentException("Invalid ID: ID too short");
        }

        return Integer.parseInt(id.substring(2));
    }

    /**
     * Get the class for the given ID by extracting the prefix and mapping it to the corresponding class.
     * 
     * @param id The ID to get the class for.
     * @return The class for the given ID.
     */
    public static Class<? extends IModel> getClass(String id) throws IllegalArgumentException {
        String prefix = getIdPrefix(id);
        return classToIdPrefix.entrySet().stream()
            .filter(entry -> entry.getValue().equals(prefix))
            .map(entry -> entry.getKey())
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Prefix not supported"));
    }
}