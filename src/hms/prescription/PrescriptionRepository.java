package hms.prescription;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;

public class PrescriptionRepository extends AbstractRepository<Prescription> {
    public PrescriptionRepository(ManagerContext ctx) {
        super(ctx);
    }

    public Prescription create(String medicalName, int quantity){
        Prescription prescription = new Prescription(medicalName, quantity);
        models.add(prescription);
        return prescription;
    }

    public void updateStatus(String id, boolean isDispensed){
        Prescription update = get(id);
        update.setStatus(isDispensed);
    }
    
    @Override
    public Prescription get(String id) {
        for(Prescription prescription : models){
            if(prescription.getId().equals(id)){
                return prescription;
            }
        }
        return null;
    }

    @Override
    public Prescription createEmptyModel() {
        return new Prescription();
    }
}
