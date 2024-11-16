package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Patient;
import java.util.Date;

/**
 * Repository class for managing patient users in the hospital management system.
 */
public class PatientRepository extends UserRepository<Patient> {

    /**
     * Constructs a new PatientRepository with the specified ManagerContext.
     *
     * @param managerContext the ManagerContext to be used by the PatientRepository
     */
    public PatientRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    /**
     * Creates a new Patient with the specified details.
     *
     * @param name the name of the Patient
     * @param isMale the gender of the Patient
     * @param contact the contact information of the Patient
     * @param dob the date of birth of the Patient
     * @return the created Patient
     */
    public Patient createPatient(String name, boolean isMale, String contact, Date dob) {
        Patient p = new Patient(ctx, name, isMale, contact, dob);

        models.add(p);

        return p;
    }

    /**
     * Creates an empty Patient model.
     *
     * @return a new empty Patient
     */
    @Override
    public Patient createEmptyModel() {
        return new Patient(this.ctx);
    }
}