package hms.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * Used to navigate through different menus
 */
public class MenuNavigator extends AbstractMenu {
    private final Stack<AbstractMenu> history = new Stack<>();
    private final HashMap<String, UserOption> navigatorOptions = new HashMap<>();

    /**
     * Constructor for MenuNavigator
     */
    public MenuNavigator() {
        this.options = new LinkedHashMap<>();

        navigatorOptions.put("b", new UserOption("Back", () -> goBack()));

        options.putAll(navigatorOptions);
    }

    /**
     * Get the current menu the user is in
     * @return the current menu to display
     */
    public AbstractMenu getCurrentMenu() {
        return history.peek();
    }

    /**
     * Add the next menu to the queue
     * @param menu the next menu
     */
    public void addMenu(AbstractMenu menu) {
        history.push(menu);
    }

    /**
     * Remove the current menu, returning to the last menu
     */
    public void popMenu() {
        if (history.size() > 1) {
            history.pop();
        }
    }

    /**
     * Returning to the last menu by removing the current menu
     */
    private void goBack() {
        if (history.size() > 1) {
            history.pop();
        }
    }

    /**
     * Update the options of the menu
     */
    private void updateOptions() {
        options.clear();
        options.putAll(history.peek().options);

        if (history.size() > 1) {
            options.putAll(navigatorOptions);
        }
    }

    @Override
    public boolean hasOption(String key) {
        return options.containsKey(key) || history.peek().hasOption(key);
    }

    @Override
    public void executeOption(String key) {
        if (options.containsKey(key)) {
            options.get(key).execute();
        } else {
            history.peek().executeOption(key);
        }
    }

    @Override
    public void display(boolean showOptions) {
        history.peek().display(false);

        if (showOptions) {
            updateOptions();

            System.out.println("\nOptions:");
            options.forEach((key, value) -> System.out.println(key + " - " + value.getText()));
        }
    }
}
