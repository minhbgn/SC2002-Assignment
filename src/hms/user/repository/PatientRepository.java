package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Patient;

import java.util.HashMap;
import java.util.Map;

public class PatientRepository extends UserRepository<Patient> {
    private Map<String, Patient> patientMap;

    public PatientRepository(ManagerContext managerContext) {
        super(managerContext);
        this.patientMap = new HashMap<>();
    }

    @Override
    public Patient createEmptyModel() {
        return new Patient(this.ctx);
    }
}
