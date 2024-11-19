package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Pharmacist;
import java.util.Date;

/**
 * Repository class for managing pharmacist users in the hospital management system.
 */
public class PharmacistRepository extends UserRepository<Pharmacist> {
    /**
     * Constructs a new PharmacistRepository with the specified ManagerContext.
     * @param managerContext the ManagerContext to be used by the PharmacistRepository
     */
    public PharmacistRepository(ManagerContext managerContext) { super(managerContext); }

    /**
     * Creates a new Pharmacist with the specified details.
     * @param name the name of the Pharmacist
     * @param isMale the gender of the Pharmacist
     * @param contact the contact information of the Pharmacist
     * @param dob the date of birth of the Pharmacist
     * @return the created Pharmacist
     */
    public Pharmacist createPharmacist(String name, boolean isMale, String contact, Date dob) {
        Pharmacist p = new Pharmacist(ctx, name, isMale, contact, dob);

        models.add(p);

        return p;
    }

    @Override
    public Pharmacist createEmptyModel() {
        return new Pharmacist(super.ctx);
    }
}