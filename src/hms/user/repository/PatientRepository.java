package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Patient;
import java.util.Date;

public class PatientRepository extends UserRepository<Patient> {
    public PatientRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    public Patient createPatient(String name, boolean isMale, String contact, Date dob) {
        Patient p = new Patient(ctx, name, isMale, contact, dob);

        models.add(p);

        return p;
    }

    @Override
    public Patient createEmptyModel() {
        return new Patient(this.ctx);
    }
}
