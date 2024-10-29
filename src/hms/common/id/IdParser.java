package hms.common.id;

import java.util.HashMap;

import hms.common.IModel;

public class IdParser {
    private static final HashMap<Class<? extends IModel>, String> classToIdPrefix = new HashMap<>();

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
    public static String getIdPrefix(String id) {
        if(id.length() <= 6) {
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
    public static int getIdSuffix(String id) {
        if(id.length() <= 6) {
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
    public static Class<? extends IModel> getClass(String id) {
        String prefix = getIdPrefix(id);
        return classToIdPrefix.entrySet().stream()
            .filter(entry -> entry.getValue().equals(prefix))
            .map(entry -> entry.getKey())
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Prefix not supported"));
    }
}