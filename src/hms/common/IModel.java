package hms.common;

import java.util.HashMap;

/**
 * Interface for model classes.
 * All model classes must implement this interface.
 */
public interface IModel {

    /**
     * Populates the model with data from the provided HashMap.
     *
     * @param data A HashMap containing the data to hydrate the model with.
     */
    public void hydrate(HashMap<String, String> data);

    /**
     * Serializes the model into a HashMap.
     *
     * @return A HashMap containing the serialized data of the model.
     */
    public HashMap<String, String> serialize();
}
