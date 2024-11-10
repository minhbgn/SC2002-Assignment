package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Admin;

public class AdminRepository extends UserRepository<Admin> {
    public AdminRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    @Override
    public Admin createEmptyModel() {
        // TODO Auto-generated method stub
        return new Admin(super.ctx);
    }
}
