package hms.ui;

import java.util.function.Consumer;

public class PaginatedListSelector<T> extends PaginatedListViewer<T>{
    private final Consumer<T> onSelect;

    public PaginatedListSelector(String title, T[] items, Consumer<T> onSelect){
        super(title, items);

        this.onSelect = onSelect;
    }

    /**
     * Checks if the given key is a valid option.
     * @param key the key to check
     * @return true if the key is a valid option, false otherwise
     */
    @Override
    public boolean hasOption(String key){
        if (super.hasOption(key)){
            return true;
        }

        try{
            int index = Integer.parseInt(key) - 1;
            return 0 <= index && index < paginatedList.getLength();
        } catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Executes the option corresponding to the given key.
     * @param key the key of the option to execute
     * @throws IllegalArgumentException if the key is not a valid option
     */
    @Override
    public void executeOption(String key){
        if (!hasOption(key))
            throw new IllegalArgumentException("Invalid option key");
            
        try{
            int index = Integer.parseInt(key) - 1;
            // Guaranteed that the index is within the bounds
            // from implementation of hasOption
            onSelect.accept(paginatedList.getItem(index));
        } catch (NumberFormatException e){
            // If the key is not a number, it must be a valid option key
            options.get(key).execute();
        }
    }
}
