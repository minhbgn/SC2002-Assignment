package hms.user.model;

import hms.common.IModel;
import java.util.HashMap;

/**
 * The account of an user. This account's id and password is used to log into the system
 */
public class Account implements IModel {
    String id = "";
    String password = "password";
    boolean isActive = true;

    /**
     * Default constructor for Account class.
     * Generates a new account without any id or password. Used for hydration.
     */
    Account() {}

    /**
     * Constructor for Account class.
     * Generates a new account with the provided id. Password is set to default value of "password".
     * @param id The id of the account.
     */
    Account(String id) {
        this.id = id;
    }

    /**
     * Getter for id attribute
     * @return The id of the account
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for isActive attribute
     * @return true if the account is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set password for the account
     * @param password The new password of the account
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the state of the account to be active or not
     * @param isActive true if the account is active, false otherwise
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }   

    /**
     * Check if input password matches Account's set password
     * @param password The input password
     * @return true if matches with Account's password, false otherwise
     */
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    @Override
    /**
     * Give the Account object its neccessary information about its attribute from a HashMap. 
     * This method is used to easily read data from external files like csv and excel into the internal model. 
     * This method does the opposite function of serialize()
     * @param data The HashMap that contains the neccessary data of the Account object
     */
    public void hydrate(HashMap<String, String> data) {
    	this.id = data.get("id");
    	this.password = data.getOrDefault("password", "no_password");
    	this.isActive = Boolean.parseBoolean(data.getOrDefault("isActive", "false"));
    }

    @Override
    /**
     * Generate a HashMap containing an Account's neccessary information
     * This method is used to easily write the item's data back to the external files like csv and excel
     * This method does the opposite function of hydrate()
     * @return The HashMap that contains every important information
     */
    public HashMap<String, String> serialize() {
    	HashMap<String, String> data = new HashMap<>();
    	data.put("id", this.id);
    	data.put("password", this.password);
    	data.put("isActive", String.valueOf(this.isActive));
    	return data;
    }
}
