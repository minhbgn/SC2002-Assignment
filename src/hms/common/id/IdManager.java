package hms.common.id;

import hms.common.IModel;

/**
 * Manager class for registering classes and generating unique IDs for them.
 * <p>
 * The generated IDs are prefixed with a two-letter code that identifies the model type.
 * This is handled by the IdParser class.
 * <p>
 * The suffix is a number that is unique to the model type (minimum 4 digits).
 * This is handled by the IdRegistry class.
 * @see IdParser
 * @see IdRegistry
 */
public class IdManager {
    /**
     * Generates a unique ID for the provided class.
     * @param clazz The class to generate
     * @return A unique ID for the class
     */
    public static String generateId(Class<? extends IModel> clazz) {
        String prefix = IdParser.getIdPrefix(clazz);
        String suffix = String.format("%04d", IdRegistry.getNextIdSuffix(clazz));

        return prefix + suffix;
    }

    /** Private constructor to prevent instantiation. */
    private IdManager() { }

    /**
     * Registers a class with the ID manager.
     * This method should be called first for each class that requires a unique ID.
     * @param clazz The class to register
     * @param prefix The prefix to use for the class
     */
    public static void registerClass(Class<? extends IModel> clazz, String prefix) {
        IdRegistry.addClass(clazz);
        IdParser.addClass(clazz, prefix);
    }
}