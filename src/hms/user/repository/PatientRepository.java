package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Patient;

public class PatientRepository extends UserRepository<Patient>{

    public PatientRepository(ManagerContext managerContext) {
        super(managerContext);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Patient createEmptyModel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyModel'");
    }
}
