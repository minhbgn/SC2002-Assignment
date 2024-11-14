package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Pharmacist;

public class PharmacistRepository extends UserRepository<Pharmacist> {
    public PharmacistRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    @Override
    public Pharmacist createEmptyModel() {
        return new Pharmacist(super.ctx);
    }
}
