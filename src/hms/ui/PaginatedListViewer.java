package hms.ui;

import java.util.HashMap;

public class PaginatedListViewer<T> extends AbstractMenu {
    private final PaginatedList<T> paginatedList;

    public PaginatedListViewer(String title, T[] items) {
        this.title = title;
        this.options = new HashMap<>();
        this.paginatedList = new PaginatedList<>(items);

        options.put("n", new UserOption("Next", () -> paginatedList.nextPage()));
        options.put("p", new UserOption("Previous", () -> paginatedList.previousPage()));
        options.put("g", new UserOption("Go to page", () -> {
            Prompt prompt = new Prompt("Enter page number: ");
            int page = prompt.getIntInput();
            paginatedList.goToPage(page);
        }));
    }

    @Override
    public void display() {
        paginatedList.display();

        System.out.println("Options:");
        options.forEach((key, value) -> System.out.println(key + " - " + value.getText()));
    }
}