package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Pharmacist;

public class PharmacistRepository extends UserRepository<Pharmacist> {
    private ManagerContext managerContext;

    public PharmacistRepository(ManagerContext managerContext) {
        super(managerContext);
        this.managerContext = managerContext;
    }

    @Override
    public Pharmacist createEmptyModel() {
        // TODO Auto-generated method stub
        return new Pharmacist(managerContext);
    }
}
