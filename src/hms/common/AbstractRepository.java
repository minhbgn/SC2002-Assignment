package hms.common;

import hms.manager.ManagerContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T extends IModel> {
    protected final ArrayList<T> models;
    protected final ManagerContext ctx;

    public AbstractRepository(ManagerContext ctx){
        this.models = new ArrayList<>();
        this.ctx = ctx;
    }

    public void parse(ArrayList<HashMap<String, String>> data){
        for(HashMap<String, String> row : data){
            T model = createEmptyModel();
            model.hydrate(row);
            models.add(model);
        }
    }

    public ArrayList<HashMap<String, String>> serialize(){
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        for(T model : models){
            data.add(model.serialize());
        }

        return data;
    }

    public List<T> findWithFilters(List<SearchCriterion<T, ?>> criteria){
        return models.stream()
            .filter(model -> criteria == null || criteria.stream().allMatch(criterion -> criterion.match(model)))
            .collect(Collectors.toList());
    }

    public abstract T get(String id);
    public abstract T createEmptyModel();
}
