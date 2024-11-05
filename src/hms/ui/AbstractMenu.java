package hms.ui;

import java.util.Map;

public abstract class AbstractMenu implements IUIElement {
    protected String title;
    protected Map<String, UserOption> options;

    public boolean hasOption(String key){
        return options.containsKey(key);
    }
    
    public void executeOption(String key){
        if (!hasOption(key)){
            throw new IllegalArgumentException("Invalid option key");
        }

        options.get(key).execute(); 
    }
}