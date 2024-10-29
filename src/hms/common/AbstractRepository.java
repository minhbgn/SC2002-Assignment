package hms.common;

import java.util.HashMap;
import java.util.List;

import hms.manager.ManagerContext;

public abstract class AbstractRepository<T extends IModel> {
    private T[] models;
    private final ManagerContext ctx;

    public AbstractRepository(ManagerContext ctx){
        this.ctx = ctx;
    }

    public void parse(HashMap<String, String> data){
        throw new UnsupportedOperationException("Not implemented");
    }

    public HashMap<String, String> serialize(){
        throw new UnsupportedOperationException("Not implemented");
    }

    public T[] findWithFilters(List<SearchCriterion<T, ?>> criteria){
        throw new UnsupportedOperationException("Not implemented");
    }

    public abstract T get(String id);
    public abstract T createEmptyModel();
}
