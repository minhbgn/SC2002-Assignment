package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Doctor;
import java.util.Date;

public class DoctorRepository extends UserRepository<Doctor>{
    public DoctorRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    public Doctor createDoctor(String name, boolean isMale, String contact, Date dob) {
        Doctor d = new Doctor(ctx, name, isMale, contact, dob);

        models.add(d);

        return d;
    }

    @Override
    public Doctor createEmptyModel() {
        return new Doctor(super.ctx);
    }

}
