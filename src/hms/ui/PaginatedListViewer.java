package hms.ui;

import java.util.HashMap;

public class PaginatedListViewer<T> extends AbstractMenu {
    protected final PaginatedList<T> paginatedList;

    public PaginatedListViewer(String title, T[] items) {
        this.title = title;
        this.options = new HashMap<>();
        this.paginatedList = new PaginatedList<>(items);

        options.put("n", new UserOption("Next", () -> paginatedList.nextPage()));
        options.put("p", new UserOption("Previous", () -> paginatedList.previousPage()));
            options.put("g", new UserOption("Go to page", () -> {
            int page = Prompt.getIntInput("Enter page number: ");
                paginatedList.goToPage(page);
            }));
    }

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