package hms.ui;

import java.util.LinkedHashMap;
import java.util.List;

public class SimpleMenu extends AbstractMenu {
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

    public void addOption(UserOption option){
        options.put(Integer.toString(options.size() + 1), option);
    }

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