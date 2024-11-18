package hms.user.model;

import hms.common.id.IdManager;
import hms.manager.*;
import java.util.*;

/**
 * This class describes an account of a Administrator. 
 * The system will recognize accounts used by this class as administrators
 * AdminService, the option of services for admins, will be given to these users by the account
 */
public class Admin extends User {
	/**
	 * Blank constructor for object to be hydrated later
	 * @param ctx The manager context to access other classes
	 */
    public Admin(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Filled constructor for the object
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
    
    /** To aid in printing out the Admin object */
    @Override
    public String toString() {
        return super.toString()+"\n User type: [Admin]";
    }
}
