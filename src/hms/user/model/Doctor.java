package hms.user.model;

import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import java.util.Date;

public class Doctor extends User {
	/**
	 * Empty constructor to be used for hydration
	 * @param ctx The manager context to let this User access other classes
	 */
    public Doctor(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Filled constructor for Doctor
     * @param ctx The manager context to let this User access other classes
     * @param name Name of the Doctor
     * @param isMale Gender of the Doctor, true if Male, false otherwise
     * @param contact Contact number of the Doctor
     * @param dob Day of birth of the Doctor
     */
    public Doctor(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.account = new Account(IdManager.generateId(Doctor.class));
    }
    
    /** To aid in printing out a Doctor object */
    @Override
    public String toString() {
        return super.toString()+"\n User type: [Doctor]";
    }
}
