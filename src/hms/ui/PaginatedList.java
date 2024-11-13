package hms.ui;

/**
 * A class that represents a paginated list of items.
 * @param <T> The type of items in the list.
 */
public class PaginatedList <T> {
    private static final int ITEMS_PER_PAGE = 15;
    private final T[] items;
    private final int pageCount;
    private int currentPage;

    /**
     * Create a new PaginatedList.
     * @param items The items to paginate.
     */
    public PaginatedList(T[] items){
        this.items = items;
        this.currentPage = 0;
        this.pageCount = (int) Math.ceil((double) items.length / ITEMS_PER_PAGE);
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
     * Go to the next page.
     */
    public void nextPage(){
        if (hasNextPage()){
            currentPage++;
        }
    }

    /**
     * Go to the previous page.
     */
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

    /**
     * Display the current page.
     */
    void display(){
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, items.length);

        for (int i = start; i < end; i++) {
            System.out.printf("%d. %s\n", i + 1, items[i]);
        }

        System.out.println("Page " + (currentPage + 1) + " of " + pageCount);
    }
}
