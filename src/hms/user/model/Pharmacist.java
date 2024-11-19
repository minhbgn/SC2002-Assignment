package hms.user.model;

import hms.common.id.IdManager;
import hms.common.id.IdParser;
import hms.common.id.IdRegistry;
import hms.manager.ManagerContext;
import java.util.*;

/** Represents a pharmacist in the hospital management system. */
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

    @Override
    public String toString() {
        return "Pharmacist " + super.toString();
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        super.hydrate(data);

        IdRegistry.tryUpdateId(Pharmacist.class, IdParser.getIdSuffix(this.account.id));
    }
}
