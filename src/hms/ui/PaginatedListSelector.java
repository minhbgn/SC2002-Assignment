package hms.ui;

public class PaginatedListSelector<T> extends PaginatedListViewer<T>{
    public PaginatedListSelector(String title, T[] items){
        super(title, items);
    }

    public T select(int index){
        if(0 <= index && index < paginatedList.getLength())
            return paginatedList.get(index);
        
        return null;
    }
}
