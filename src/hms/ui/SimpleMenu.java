package hms.ui;

import java.util.LinkedHashMap;
import java.util.List;

public class SimpleMenu extends AbstractMenu {
    public SimpleMenu(String title, List<UserOption> options){
        this.title = title;
        this.options = new LinkedHashMap<>();

        for (int i = 0; i < options.size(); i++){
            this.options.put(Integer.toString(i + 1), options.get(i));
        }
    }

    @Override
    public void display(boolean showOptions) {
        System.out.println(title);

        if (showOptions){
            options.forEach((key, value) -> System.out.println(key + ". " + value.getText()));
        }
    }
}