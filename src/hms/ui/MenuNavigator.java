package hms.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

public class MenuNavigator extends AbstractMenu {
    private final Stack<AbstractMenu> history = new Stack<>();
    private final HashMap<String, UserOption> navigatorOptions = new HashMap<>();

    public MenuNavigator() {
        this.options = new LinkedHashMap<String, UserOption>();

        navigatorOptions.put("b", new UserOption("Back", () -> goBack()));

        options.putAll(navigatorOptions);
    }

    public void addMenu(AbstractMenu menu) {
        history.push(menu);
    }

    public void popMenu() {
        if (history.size() > 1) {
            history.pop();
        }
    }

    private void goBack() {
        if (history.size() > 1) {
            history.pop();
        }
    }

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
