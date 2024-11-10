package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Patient;

public class PatientRepository extends UserRepository<Patient>{

    private ManagerContext managerContext;
    
        public PatientRepository(ManagerContext managerContext) {
            super(managerContext);
            this.managerContext = managerContext;
    }

    @Override
    public Patient createEmptyModel() {
        // TODO Auto-generated method stub
        return new Patient(managerContext);
    }
}
