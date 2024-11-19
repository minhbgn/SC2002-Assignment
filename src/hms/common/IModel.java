package hms.common;

import java.util.HashMap;

/**
 * Interface for model classes. All model classes must implement this interface.
 * <p>
 * A model represents an entity in the system and is used to store and manipulate data.
 * The model should only be able to provide the following functionalities:
 * 
 * - Hydrate: Populate the current instance of the model with data from a HashMap.
 * 
 * - Serialize: Convert the current instance of the model into a HashMap.
 * 
 * - Getters (or public fields): Provide access to the data stored in the model.
 * 
 * - Setters (should be package-private): Allow the model's data to be modified. The package-private
 *  access modifier is used to restrict the modification of data only to the package in which
 *  the model resides, which the repository classes are part of. If the repository classes are
 *  in a different package, the setters can be made public.
 * 
 * - Constructors (should be package-private): Allow the creation of new instances of the model.
 * The package-private access modifier is used to restrict the creation of new instances only to
 * the package in which the model resides, which the repository classes are part of. If the repository
 * classes are in a different package, the constructors can be made public.
 */
public interface IModel {

    /**
     * Populates the current instance of the model with data from the provided HashMap.
     * <p>
     * The keys in the HashMap should match with the ones used in the serialize method.
     * <p>
     * Models containing nested models should call the hydrate method of the nested model
     * instead of directly setting the value or calling the constructor.
     * @param data A HashMap containing the data to hydrate the model with.
     */
    public void hydrate(HashMap<String, String> data);

    /**
     * Serializes the model into a HashMap containing the data of the model.
     * <p>
     * The keys in the HashMap will be the ones used in the hydrate method
     * and nowhere else.
     * <p>
     * Models containing nested models should call the serialize method of the nested model
     * instead of reconstructing the key-value pairs by themselves.
     * @return A HashMap containing the serialized data of the model.
     */
    public HashMap<String, String> serialize();
}
