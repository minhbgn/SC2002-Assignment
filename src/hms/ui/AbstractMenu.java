package hms.ui;

import java.util.Map;

/**
 * The abstract menu class for simple menu class to build upon
 */
public abstract class AbstractMenu implements IUIElement {
	/**
	 * The title of the menu. Shows the user which menu they are in
	 */
    public String title;
    protected Map<String, UserOption> options;

    /**
     * Set the title of the Menu
     * @param title the title of the Menu
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Check if the Menu has a specified option
     * @param key name of the specified option
     * @return true if the Menu has said option, false otherwise
     */
    public boolean hasOption(String key){
        return options.containsKey(key);
    }

    /**
     * Add an option into the menu
     * @param key name of the option
     * @param option the executer of the option. Usually calling those classes in system.service packages
     */
    public void addOption(String key, UserOption option){
        options.put(key, option);
    }

    /**
     * Get the hash map containing the options with their name and executor
     * @return the hash map of options, containing the key: name of the option, and the value: the executor
     */
    public Map<String, UserOption> getOptions(){
        return options;
    }
    
    /**
     * Execute the option
     * @param key the name of the option to execute
     */
    public void executeOption(String key){
        if (!hasOption(key)){
            throw new IllegalArgumentException("Invalid option key");
        }

        options.get(key).execute(); 
    }
}