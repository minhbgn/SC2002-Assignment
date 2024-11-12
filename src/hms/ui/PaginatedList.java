package hms.ui;

public class PaginatedList <T> {
    private static final int ITEMS_PER_PAGE = 15;
    private final T[] items;
    private final int pageCount;
    private int currentPage;

    public PaginatedList(T[] items){
        this.items = items;
        this.currentPage = 0;
        this.pageCount = (int) Math.ceil((double) items.length / ITEMS_PER_PAGE);
    }

    public T get(int index){
        if (index < 0 || index >= items.length){
            throw new IllegalArgumentException("Invalid index");
        }

        return items[index];
    }

    public int getLength(){
        return items.length;
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
