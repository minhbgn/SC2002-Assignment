package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Admin;
import java.util.Date;

/**
 * Repository class for managing admin users in the hospital management system.
 */
public class AdminRepository extends UserRepository<Admin> {

    /**
     * Constructs a new AdminRepository with the specified ManagerContext.
     *
     * @param managerContext the ManagerContext to be used by the AdminRepository
     */
    public AdminRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    /**
     * Creates a new Admin with the specified details.
     *
     * @param name the name of the Admin
     * @param isMale the gender of the Admin
     * @param contact the contact information of the Admin
     * @param dob the date of birth of the Admin
     * @return the created Admin
     */
    public Admin createAdmin(String name, boolean isMale, String contact, Date dob) {
        Admin a = new Admin(ctx, name, isMale, contact, dob);

        models.add(a);

        return a;
    }

    /**
     * Creates an empty Admin model.
     *
     * @return a new empty Admin
     */
    @Override
    public Admin createEmptyModel() {
        return new Admin(super.ctx);
    }
}