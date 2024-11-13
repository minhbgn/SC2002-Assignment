package hms.ui;

import java.util.function.Consumer;

public class PaginatedListSelector<T> extends PaginatedListViewer<T>{
    private final Consumer<T> onSelect;

    public PaginatedListSelector(String title, T[] items, Consumer<T> onSelect){
        super(title, items);

        this.onSelect = onSelect;
    }

    @Override
    public void executeOption(String key){
        if (hasOption(key)){
            options.get(key).execute();
        }

        try{
            int index = Integer.parseInt(key) - 1;
            if (0 <= index && index < paginatedList.getLength()){
                onSelect.accept(paginatedList.getItem(index));
            }
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid option");
        }
    }
}
