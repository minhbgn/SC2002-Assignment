package hms.ui;

import java.util.Map;

/**
 * Abstract class for a menu
 */
public abstract class AbstractMenu implements IUIElement {
    /** Title of the menu */
    public String title;
    /** Options of the menu */
    protected Map<String, UserOption> options;

    // Getters
    public boolean hasOption(String key){ return options.containsKey(key); }
    public Map<String, UserOption> getOptions(){ return options; }
    
    // Setters
    public void addOption(String key, UserOption option){ options.put(key, option); }
    public void setTitle(String title){ this.title = title; }

    /**
     * Execute an option
     * @param key Key of the option to execute
     * @throws IllegalArgumentException If the key is invalid
     */
    public void executeOption(String key){
        if (!hasOption(key)){
            throw new IllegalArgumentException("Invalid option key");
        }

        options.get(key).execute(); 
    }
}