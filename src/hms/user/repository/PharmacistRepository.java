package hms.user.repository;

import hms.manager.ManagerContext;
import hms.user.model.Pharmacist;
import java.util.Date;

public class PharmacistRepository extends UserRepository<Pharmacist> {
    public PharmacistRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    public Pharmacist createPharmacist(String name, boolean isMale, String contact, Date dob) {
        Pharmacist p = new Pharmacist(ctx, name, isMale, contact, dob);

        models.add(p);

        return p;
    }

    @Override
    public Pharmacist createEmptyModel() {
        return new Pharmacist(super.ctx);
    }
}
