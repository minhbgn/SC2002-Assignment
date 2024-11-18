package hms.common;

import hms.manager.ManagerContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract repository class for managing models in the hospital management system.
 *
 * @param <T> the type of model that the repository manages
 */
public abstract class AbstractRepository<T extends IModel> {
    protected final ArrayList<T> models;
    protected final ManagerContext ctx;

    /**
     * Constructs a new AbstractRepository with the specified ManagerContext.
     *
     * @param ctx the ManagerContext to be used by the repository
     */
    public AbstractRepository(ManagerContext ctx) {
        this.models = new ArrayList<>();
        this.ctx = ctx;
    }

    /**
     * Parses the provided data and populates the repository with models.
     *
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
     *
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
}