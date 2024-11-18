package hms.ui;

import hms.common.SearchCriterion;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A class that represents a paginated list of items.
 * @param <T> The type of items in the list.
 */
public final class PaginatedList <T> {
    private static final int DEFAULT_ITEMS_PER_PAGE = 15;

    /** The number of items displayed per page. */
    private final int itemsPerPage;
    /** All items to paginate. */
    private final T[] items;
    /** The items to display. This is a subset of items based on the search criteria and sorter. */
    private T[] displayedItems;
    /** The number of pages. */
    private int pageCount;
    /** The current page number. */
    private int currentPage;

    /** The active search criteria for the list. */
    List<SearchCriterion<T, ?>> criteria;
    /** The active sorter for the list. */
    Function<T, ? extends Comparable<?>> sorter;

    /**
     * Create a new PaginatedList.
     * @param items The items to paginate.
     */
    public PaginatedList(T[] items){
        this.items = items;
        this.itemsPerPage = DEFAULT_ITEMS_PER_PAGE;
        this.currentPage = 0;
        
        this.criteria = null;
        this.sorter = null;

        updateList();
    }

    /**
     * Create a new PaginatedList
     * @param items The items to paginate
     * @param itemsPerPage Number of items to display per page
     */
    public PaginatedList(T[] items, int itemsPerPage){
        this.items = items;
        this.itemsPerPage = itemsPerPage;
        this.currentPage = 0;
        
        this.criteria = null;
        this.sorter = null;

        updateList();
    }

    /** Update the list based on the search criteria and sorter. */
    @SuppressWarnings("unchecked")
    public void updateList() {
        // Filter and sort the items
        displayedItems = (T[]) Arrays.stream(items)
            .filter(item -> criteria == null || criteria.stream().allMatch(criterion -> criterion.match(item)))
            .toArray();
        
        currentPage = 0;
        pageCount = (int) Math.ceil((double) displayedItems.length / itemsPerPage);
        
        // If there is no sorter, skip sorting
        if(sorter == null) return;

        // Sort the items
        Arrays.sort(displayedItems, (a, b) -> {
            // Null checks
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            if (b == null) return 1;

            // Get the keys
            Comparable<Object> keyA = (Comparable<Object>) sorter.apply(a);
            Comparable<Object> keyB = (Comparable<Object>) sorter.apply(b);

            // More null checks
            if (keyA == null && keyB == null) return 0;
            if (keyA == null) return -1;
            if (keyB == null) return 1;
            
            return keyA.compareTo(keyB);
        });
    }

    /**
     * Get the item at the specified index.
     * @param index The index of the item to get. Starts from 0.
     * @return The item at the specified index.
     * @throws IllegalArgumentException if the index is invalid.
     */
    public T getItem(int index){
        if (index < 0 || index >= items.length){
            throw new IllegalArgumentException("Invalid index");
        }

        return items[index];
    }

    /**
     * Check if there is a previous page.
     * @return true if there is a previous page, false otherwise.
     */
    public boolean hasPreviousPage(){
        return currentPage > 0;
    }

    /**
     * Check if there is a next page.
     * @return true if there is a next page, false otherwise.
     */
    public boolean hasNextPage(){
        return currentPage < pageCount - 1;
    }

    /**
     * Get the current page number.
     * @return The current page number. Starts from 0.
     */
    public int getCurrentPage(){
        return currentPage;
    }

    /**
     * Get the total number of pages.
     * @return The total number of pages.
     */
    public int getLength(){
        return items.length;
    }

    /**
     * Set the search criteria for the list. If the criteria is null, the list will not be filtered.
     * @param criteria The search criteria to set.
     */
    public void setCriteria(List<SearchCriterion<T, ?>> criteria) {
        this.criteria = criteria;
        updateList();
    }

    /**
     * Add a search criterion to the list.
     * @param criterion The search criterion to add.
     */
    public void addCriteria(SearchCriterion<T, ?> criterion) {
        if (criteria == null) criteria = List.of();

        criteria.add(criterion);
        updateList();
    }

    /**
     * Set the sorter for the list. If the sorter is null, the list will not be sorted.
     * @param sorter The sorter to set.
     */
    public void setSorter(Function<T, ? extends Comparable<?>> sorter) {
        this.sorter = sorter;
        updateList();
    }
    
    /** Go to the next page. */
    public void nextPage(){
        if (hasNextPage()){
            currentPage++;
        }
    }

    /** Go to the previous page. */
    public void previousPage(){
        if (hasPreviousPage()){
            currentPage--;
        }
    }

    /**
     * Go to a specific page.
     * @param page The page number to go to. Starts from 0.
     * @throws IllegalArgumentException if the page number is invalid. 
     */
    public void goToPage(int page){
        if (!isValidPage(page)){
            throw new IllegalArgumentException("Invalid page number");
        }

        currentPage = page;
    }

    /**
     * Check if the specified page number is valid.
     * @param page The page number to check. Starts from 0.
     * @return true if the page number is valid, false otherwise.
     */
    public boolean isValidPage(int page){
        return page >= 0 && page < pageCount;
    }

    /** Display the current page. */
    void display(){
        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.length);

        for (int i = start; i < end; i++) {
            System.out.printf("%d. %s\n", i + 1, items[i]);
        }

        System.out.println("Page " + (currentPage + 1) + " of " + pageCount);
    }
}
