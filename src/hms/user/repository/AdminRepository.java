package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Admin;

public class AdminRepository extends UserRepository<Admin> {
    private ManagerContext managerContext;
    
        public AdminRepository(ManagerContext managerContext) {
            super(managerContext);
            this.managerContext = managerContext;
    }

    @Override
    public Admin createEmptyModel() {
        // TODO Auto-generated method stub
        return new Admin(managerContext);
    }
}
