package hms.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

public class MenuNavigator extends AbstractMenu {
    private final Stack<AbstractMenu> history = new Stack<>();
    private final HashMap<String, UserOption> navigatorOptions = new HashMap<>();

    public MenuNavigator(String title) {
        this.title = title;
        this.options = new LinkedHashMap<>();

        navigatorOptions.put("b", new UserOption("Back", () -> {
            if (history.size() > 1) {
                goBack();
            }
            else{
                System.out.println("Cannot go back");
            }
        }));

        options.putAll(navigatorOptions);
    }

    public void addMenu(AbstractMenu menu) {
        history.push(menu);
        
        options.clear();
        options.putAll(menu.options);
        if (history.size() > 1) {
            options.putAll(navigatorOptions);
        }
    }

    private void goBack() {
        if (history.size() > 1) {
            history.pop();

            options.clear();
            options.putAll(history.peek().options);

            if (history.size() > 1) {
                options.putAll(navigatorOptions);
            }
        }
        else{
            throw new IllegalStateException("Cannot go back from root menu");
        }
    }

    @Override
    public void display() {
        System.out.println(title);

        System.out.println("Options:");
        options.forEach((key, value) -> System.out.println(key + " - " + value.getText()));
    }
}
