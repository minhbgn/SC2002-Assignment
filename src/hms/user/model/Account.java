package hms.user.model;

import java.util.HashMap;

import hms.common.IModel;

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

    public String getId() {
        return id;
    }

    void setPassword(String password) {
        this.password = password;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        throw new UnsupportedOperationException("Unimplemented method 'hydrate'");
    }

    @Override
    public HashMap<String, String> serialize() {
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }
}
