package hms.ui;

import hms.common.SearchCriterion;
import java.util.HashMap;
import java.util.function.Function;

public class PaginatedListViewer<T> extends AbstractMenu {
    protected final PaginatedList<T> paginatedList;

    /**
     * Constructs a PaginatedListViewer with the specified title and items.
     *
     * @param title the title of the viewer
     * @param items the items to be displayed in the viewer
     */
    public PaginatedListViewer(String title, T[] items) {
        this.title = title;
        this.options = new HashMap<>();
        this.paginatedList = new PaginatedList<>(items);

        updateOptions();
    }

    /**
     * Adds a filter to the paginated list.
     *
     * @param filter the filter to be added
     */
    public void addFilter(SearchCriterion<T, ?> filter) {
        paginatedList.addCriteria(filter);
    }

    /**
     * Clears all filters from the paginated list.
     */
    public void clearFilters() {
        paginatedList.setCriteria(null);
    }

    /**
     * Sorts the paginated list by the specified key extractor.
     *
     * @param keyExtractor the function to extract the key for sorting
     */
    public void sortBy(Function<T, Comparable<?>> keyExtractor) {
        paginatedList.setSorter(keyExtractor);
    }

    /**
     * Clears the sorter from the paginated list.
     */
    public void clearSorter() {
        paginatedList.setSorter(null);
    }

    /**
     * Updates the options for pagination controls.
     */
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