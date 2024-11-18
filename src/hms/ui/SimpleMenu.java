package hms.ui;

import java.util.LinkedHashMap;
import java.util.List;

public class SimpleMenu extends AbstractMenu {
    /**
     * Constructs a SimpleMenu with a title and a list of user options.
     * @param title the title of the menu
     * @param options the list of user options
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
     * Adds a new option to the menu.
     * @param option the user option to add
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