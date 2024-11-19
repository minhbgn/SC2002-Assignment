package hms.user.model;

import hms.common.id.IdManager;
import hms.common.id.IdParser;
import hms.common.id.IdRegistry;
import hms.manager.*;
import java.util.*;

/**
 * Admin class to represent the Admin user type
 */
public class Admin extends User {
	/**
	 * Blank constructor for object to be hydrated later
	 * @param ctx The manager context to access other classes
	 */
    public Admin(ManagerContext ctx) { super(ctx); }

    /**
     * Constructor to create a new Admin object
     * @param ctx The manager context to access other classes
     * @param name Name of Admin
     * @param isMale Gender of Admin: true if Male, false otherwise
     * @param contact Contact number of Admin
     * @param dob Day of birth of Admin
     */
    public Admin(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.account = new Account(IdManager.generateId(Admin.class));
    }
    
    @Override
    public String toString() {
        return "Admin " + super.toString();
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        super.hydrate(data);

        IdRegistry.tryUpdateId(Admin.class, IdParser.getIdSuffix(this.account.id));
    }
}
