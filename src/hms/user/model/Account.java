package hms.user.model;

import hms.common.IModel;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Represents an account in the system.
 * Cannot be instantiated form outside the package.
 */
public class Account implements IModel {
    /** The id of the account. This acts as the id for the user. */
    String id = "";
    /** The password of the account. */
    String password = "password";
    /** The status of the account. */
    boolean isActive = true;

    /** Default constructor for Account class. Used for hydration. */
    Account() {}

    /**
     * Constructor for Account class.
     * Generates a new account with the provided id. Password is set to default value of "password".
     * @param id The id of the account.
     */
    Account(String id) { this.id = id; }

    public String getId() { return id; }
    public boolean isActive() { return isActive; }

    public void setPassword(String password) { this.password = password; }
    public void setActive(boolean isActive) { this.isActive = isActive; }   

    /**
     * Check if input password matches Account's set password
     * @param password The input password
     * @return true if matches with Account's password, false otherwise
     */
    public boolean authenticate(String password) { return this.password.equals(password); }

    @Override
    public void hydrate(HashMap<String, String> data) {
    	this.id = data.get("id");
    	this.password = data.getOrDefault("password", "no_password");
    	this.isActive = Boolean.parseBoolean(data.getOrDefault("isActive", "false"));
    }

    @Override
    public HashMap<String, String> serialize() {
    	HashMap<String, String> data = new LinkedHashMap<>();
    	data.put("id", this.id);
    	data.put("password", this.password);
    	data.put("isActive", String.valueOf(this.isActive));
    	return data;
    }
}
