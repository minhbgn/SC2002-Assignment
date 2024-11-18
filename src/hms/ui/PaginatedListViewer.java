package hms.ui;

import hms.common.SearchCriterion;
import java.util.HashMap;
import java.util.function.Function;

/**
 * A menu that displays a paginated list of items.
 * @param <T> The type of items in the list
 */
public class PaginatedListViewer<T> extends AbstractMenu {
    /** The title of the menu */
    protected final PaginatedList<T> paginatedList;

    /**
     * Creates a new PaginatedListViewer with the specified title and items.
     * @param title The title of the menu
     * @param items The items to display
     */
    public PaginatedListViewer(String title, T[] items) {
        this.title = title;
        this.options = new HashMap<>();
        this.paginatedList = new PaginatedList<>(items);

        updateOptions();
    }

    /**
     * Adds a filter to the list of filters.
     * @param filter The filter to add
     */
    public void addFilter(SearchCriterion<T, ?> filter) { paginatedList.addCriteria(filter); }
    /**
     * Sorts the items based on the specified key extractor.
     * @param keyExtractor The key extractor to use for sorting
     */
    public void sortBy(Function<T, Comparable<?>> keyExtractor) { paginatedList.setSorter(keyExtractor); }
    
    /** Clears all filters. */
    public void clearFilters() { paginatedList.setCriteria(null); }
    /** Clears the sorter. */
    public void clearSorter() { paginatedList.setSorter(null); }

    /** Updates the options based on the current state of the paginated list. */
    private void updateOptions() {
        // Remove existing options
        options.remove("n");
        options.remove("p");
        options.remove("g");

        // Add new options
        // Only show next page option if there is a next page
        if(paginatedList.hasNextPage()){
            options.put("n", new UserOption("Next", () -> {
                paginatedList.nextPage();

                updateOptions();
            }));
        }
        // Only show previous page option if there is a previous page
        if(paginatedList.hasPreviousPage()){
            options.put("p", new UserOption("Previous", () -> {
                paginatedList.previousPage();

                updateOptions();
            }));
        }
        // Only show go to page option if there is more than one page
        if(paginatedList.isValidPage(1)){
            options.put("g", new UserOption("Go to page", () -> {
                int page = Prompt.getIntInput("Enter page number: ") - 1; // 0-based index
                if (!paginatedList.isValidPage(page)){
                    System.out.println("Invalid page number");
                    return;
                }

                paginatedList.goToPage(page);
                updateOptions();
            }));
        }
    }

    /**
     * Displays the paginated list and optionally shows the pagination options.
     *
     * @param showOptions whether to show the pagination options
     */
    @Override
    public void display(boolean showOptions) {
        System.out.println(title + '\n');

        paginatedList.display();

        if (showOptions){
            System.out.println("\nOptions:");
            options.forEach((key, value) -> {
                System.out.println(key + " - " + value.getText());
            });
        }
    }
}