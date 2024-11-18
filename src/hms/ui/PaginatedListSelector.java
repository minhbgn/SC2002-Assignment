package hms.ui;

import java.util.function.Consumer;

/**
 * A paginated list viewer that allows the user to select an item from the list.
 * <p>
 * It inherits from {@link PaginatedListViewer} and adds the ability to select an item from the list.
 * @param <T> The type of items in the list
 */
public class PaginatedListSelector<T> extends PaginatedListViewer<T>{
    /** The action to perform when an item is selected */
    private final Consumer<T> onSelect;

    /**
     * Creates a new paginated list selector.
     * @param title The title of the list
     * @param items The items in the list
     * @param onSelect The action to perform when an item is selected
     */
    public PaginatedListSelector(String title, T[] items, Consumer<T> onSelect){
        super(title, items);

        this.onSelect = onSelect;
    }

    /**
     * Checks if the given key is a valid option key.
     * <p>
     * Overrides the default implementation to allow for selecting items by index.
     * @param key The key to check
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
     * Executes the option with the given key.
     * <p>
     * Overrides the default implementation to allow for selecting items by index.
     * @param key The key of the option to execute
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
