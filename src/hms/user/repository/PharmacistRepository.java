package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Pharmacist;

public class PharmacistRepository extends UserRepository<Pharmacist> {
    public PharmacistRepository(ManagerContext managerContext) {
        super(managerContext);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Pharmacist createEmptyModel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyModel'");
    }
}
