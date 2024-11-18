package hms.user.model;

import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import java.util.*;

public class Pharmacist extends User {
	/**
	 * Default constructor for Pharmacist.
	 * @param ctx Manager context to access other classes
	 */
    public Pharmacist(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Attribute-filled constructor for Pharmacist
     * @param ctx Manager context to access other classes
     * @param name Name of the Pharmacist
     * @param isMale Gender of the Pharmacist: True if Male, False otherwise
     * @param contact Contact number of the Pharmacist
     * @param dob Day of birth of the Pharmacist
     */
    public Pharmacist(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.account = new Account(IdManager.generateId(Pharmacist.class));
    }

    /** Function to print out Pharmacist */
    @Override
    public String toString() {
        return super.toString()+"\n User type: [Pharmacist]";
    }
}
