package hms.ui;

import java.util.LinkedHashMap;
import java.util.List;

/** A simple menu that displays a list of options. */
public class SimpleMenu extends AbstractMenu {
    /**
     * Creates a new SimpleMenu with the specified title and options.
     * @param title The title of the menu
     * @param options The options to display
     */
    public SimpleMenu(String title, List<UserOption> options){
        this.title = title;
        this.options = new LinkedHashMap<>();

        if (options == null){
            return;
        }
        
        for (int i = 0; i < options.size(); i++){
            this.options.put(Integer.toString(i + 1), options.get(i));
        }
    }

    /**
     * Adds an option to the menu.
     * @param option The option to add
     */
    public void addOption(UserOption option){
        options.put(Integer.toString(options.size() + 1), option);
    }

    /**
     * Displays the menu title and options if showOptions is true.
     * @param showOptions whether to display the options
     */
    @Override
    public void display(boolean showOptions) {
        System.out.println(title);

        if (showOptions){
            options.forEach(
                (key, value) -> System.out.println(
                String.format(
                    key.matches("\\d+") ? "%s. %s" : "%s - %s",
                    key, value.getText()
                )
            ));
        }
    }
}