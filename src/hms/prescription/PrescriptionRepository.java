package hms.prescription;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;

public class PrescriptionRepository extends AbstractRepository<Prescription> {
    public PrescriptionRepository(ManagerContext ctx) {
        super(ctx);
    }

    public Prescription create(String medicalName, int quantity){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Prescription get(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Prescription createEmptyModel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyModel'");
    }
}
