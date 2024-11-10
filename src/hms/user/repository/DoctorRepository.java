package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Doctor;

public class DoctorRepository extends UserRepository<Doctor>{
    public DoctorRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    @Override
    public Doctor createEmptyModel() {
        // TODO Auto-generated method stub
        return new Doctor(super.ctx);
    }

}
