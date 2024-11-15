package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Admin;
import java.util.Date;

public class AdminRepository extends UserRepository<Admin> {
    public AdminRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    public Admin createAdmin(String name, boolean isMale, String contact, Date dob) {
        Admin a = new Admin(ctx, name, isMale, contact, dob);

        models.add(a);

        return a;
    }

    @Override
    public Admin createEmptyModel() {
        return new Admin(super.ctx);
    }
}
