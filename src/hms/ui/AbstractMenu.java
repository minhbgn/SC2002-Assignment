package hms.ui;

import java.util.Map;

public abstract class AbstractMenu implements IUIElement {
    public String title;
    protected Map<String, UserOption> options;

    public void setTitle(String title){
        this.title = title;
    }

    public boolean hasOption(String key){
        return options.containsKey(key);
    }

    public void addOption(String key, UserOption option){
        options.put(key, option);
    }

    public Map<String, UserOption> getOptions(){
        return options;
    }
    
    public void executeOption(String key){
        if (!hasOption(key)){
            throw new IllegalArgumentException("Invalid option key");
        }

        options.get(key).execute(); 
    }
}