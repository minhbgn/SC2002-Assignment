package hms.ui;

public class PaginatedList {
    private static final int ITEMS_PER_PAGE = 15;
    private final Object[] items;
    private final int pageCount;
    private int currentPage;

    public PaginatedList(Object[] items){
        this.items = items;
        this.currentPage = 0;
        this.pageCount = (int) Math.ceil((double) items.length / ITEMS_PER_PAGE);
    }

    public void nextPage(){
        if (currentPage < pageCount - 1){
            currentPage++;
        }
    }

    public void previousPage(){
        if (currentPage > 0){
            currentPage--;
        }
    }

    public void goToPage(int page){
        if (!isValidPage(page)){
            throw new IllegalArgumentException("Invalid page number");
        }

        currentPage = page;
    }

    public boolean isValidPage(int page){
        return page >= 0 && page < pageCount;
    }

    void display(){
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, items.length);

        for (int i = start; i < end; i++) {
            System.out.printf("%d. %s\n", i + 1, items[i]);
        }

        System.out.println("Page " + (currentPage + 1) + " of " + pageCount);
    }
}
