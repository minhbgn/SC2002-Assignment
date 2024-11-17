package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Doctor;
import java.util.Date;

/**
 * Repository class for managing doctor users in the hospital management system.
 */
public class DoctorRepository extends UserRepository<Doctor>{

    /**
     * Constructs a new DoctorRepository with the specified ManagerContext.
     *
     * @param managerContext the ManagerContext to be used by the DoctorRepository
     */
    public DoctorRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    /**
     * Creates a new Doctor with the specified details.
     *
     * @param name the name of the Doctor
     * @param isMale the gender of the Doctor
     * @param contact the contact information of the Doctor
     * @param dob the date of birth of the Doctor
     * @return the created Doctor
     */
    public Doctor createDoctor(String name, boolean isMale, String contact, Date dob) {
        Doctor d = new Doctor(ctx, name, isMale, contact, dob);

        models.add(d);

        return d;
    }

    /**
     * Creates an empty Doctor model.
     *
     * @return a new empty Doctor
     */
    @Override
    public Doctor createEmptyModel() {
        return new Doctor(super.ctx);
    }
}