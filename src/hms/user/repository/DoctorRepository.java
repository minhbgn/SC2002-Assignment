package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Doctor;

public class DoctorRepository extends UserRepository<Doctor>{
    private ManagerContext managerContext;
    
        public DoctorRepository(ManagerContext managerContext) {
            super(managerContext);
            this.managerContext = managerContext;
    }

    @Override
    public Doctor createEmptyModel() {
        // TODO Auto-generated method stub
        return new Doctor(managerContext);
    }

}
