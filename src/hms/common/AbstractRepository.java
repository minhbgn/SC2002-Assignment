package hms.common;

import hms.manager.ManagerContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract repository class for storing and managing models of a specific type. Concrete
 * repository classes should extend this class bound to the appropriate type of model,
 * and implement the required methods.
 * <p>
 * All models (defined as classes implementing the IModel interface) are stored in the repository,
 * and cannot be accessed without going through the repository. The repository provides methods
 * for creating, reading, updating, and (optionally) deleting models (CRUD operations). Models
 * cannot be created, updated, or deleted directly; however, they can be retrieved and have their
 * internal values read through getter methods.
 * <p>
 * The repository is also has methods for parsing and serializing the models, which are used
 * for reading and writing data to and from storage.
 * @param <T> the type of model (defined above) that the repository manages
 */
public abstract class AbstractRepository<T extends IModel> {
    /** The list of models stored in the repository. */
    protected final ArrayList<T> models;
    /** The ManagerContext used by the repository. */
    protected final ManagerContext ctx;

    /**
     * Constructs a new AbstractRepository with the specified ManagerContext.
     * @param ctx the ManagerContext to be used by the repository
     */
    public AbstractRepository(ManagerContext ctx) {
        this.models = new ArrayList<>();
        this.ctx = ctx;
    }

    /**
     * Parses the provided data and populates the repository with models.
     * @param data an ArrayList of HashMaps containing the data to populate the models
     */
    public void parse(ArrayList<HashMap<String, String>> data) {
        for (HashMap<String, String> row : data) {
            T model = createEmptyModel();
            model.hydrate(row);
            models.add(model);
        }
    }

    /**
     * Serializes the models in the repository into an ArrayList of HashMaps.
     * @return an ArrayList of HashMaps containing the serialized data of the models
     */
    public ArrayList<HashMap<String, String>> serialize() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        for (T model : models) {
            data.add(model.serialize());
        }

        return data;
    }

    /**
     * Finds models in the repository that match the specified search criteria.
     *
     * @param criteria a list of search criteria to filter the models
     * @return a list of models that match the search criteria
     */
    public List<T> findWithFilters(List<SearchCriterion<T, ?>> criteria) {
        return models.stream()
            .filter(model -> criteria == null || criteria.stream().allMatch(criterion -> criterion.match(model)))
            .collect(Collectors.toList());
    }

    /**
     * Retrieves the model with the specified ID.
     *
     * @param id the ID of the model
     * @return the model with the specified ID, or null if not found
     */
    public abstract T get(String id);
    
    /**
     * Creates a blank model of the appropriate type. Used for hydration.
     * @return A new, empty model of the appropriate type
     */    
    public abstract T createEmptyModel();
}