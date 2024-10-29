package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Doctor;

public class DoctorRepository extends UserRepository<Doctor>{
    public DoctorRepository(ManagerContext managerContext) {
        super(managerContext);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Doctor createEmptyModel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyModel'");
    }

}
