package hms.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * MenuNavigator class is a class that allows the user to navigate through the menus.
 * It keeps track of the history of the menus visited by the user.
 */
public class MenuNavigator extends AbstractMenu {
    /** The stack to keep track of the history of the menus visited by the user. */
    private final Stack<AbstractMenu> history = new Stack<>();
    /** The options available to the user in the current menu. */
    private final HashMap<String, UserOption> navigatorOptions = new HashMap<>();

    /**
     * Initializes the MenuNavigator class.
     * <p>
     * It initializes the navigatorOptions with the "Back" option.
     */
    public MenuNavigator() {
        this.options = new LinkedHashMap<>();

        navigatorOptions.put("b", new UserOption("Back", () -> popMenu()));

        options.putAll(navigatorOptions);
    }

    /** 
     * Returns the current menu.
     * @return The current menu.
     */
    public AbstractMenu getCurrentMenu() { return history.peek(); }

    /**
     * Adds a menu to the history stack. This method is used to navigate to a new menu.
     * @param menu The menu to be added to the history stack.
     */
    public void addMenu(AbstractMenu menu) { history.push(menu); }

    /**
     * Pops the current menu from the history stack.
     * This method is used to navigate back to the previous menu.
     * If the history stack has only one menu, it does nothing.
     */
    public void popMenu() {
        if (history.size() > 1) {
            history.pop();
        }
    }

    /**
     * Updates the options available to the user based on the current menu.
     * This is to update the display of the options with
     * the options available in the current menu.
     */
    private void updateOptions() {
        options.clear();
        options.putAll(history.peek().options);

        if (history.size() > 1) {
            options.putAll(navigatorOptions);
        }
    }

    /**
     * Checks if the given key is an option in the current menu or in the history.
     * @param key The key to check if it is an option.
     * @return True if the key is an option, false otherwise.
     */
    @Override
    public boolean hasOption(String key) {
        return options.containsKey(key) || history.peek().hasOption(key);
    }

    /**
     * Executes the option with the given key.
     * If the key is not an option in the navigator, it checks in the history options
     * and executes the option if found.
     * @param key The key of the option
     */
    @Override
    public void executeOption(String key) {
        if (options.containsKey(key)) {
            options.get(key).execute();
        } else {
            history.peek().executeOption(key);
        }
    }

    /**
     * Displays the current menu.
     * If showOptions is true, it displays the options available to the user.
     * The overridden display method displays the current menu and the options available.
     * @param showOptions Whether to display the options or not.
     */
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
