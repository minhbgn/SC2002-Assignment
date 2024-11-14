package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Patient;

public class PatientRepository extends UserRepository<Patient> {
    public PatientRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    @Override
    public Patient createEmptyModel() {
        return new Patient(this.ctx);
    }
}
