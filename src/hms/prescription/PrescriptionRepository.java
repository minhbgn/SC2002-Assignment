package hms.prescription;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;

/**
 * Repository class for managing prescriptions in the hospital management system.
 */
public class PrescriptionRepository extends AbstractRepository<Prescription> {
    
    /**
     * Constructs a new PrescriptionRepository with the specified ManagerContext.
     *
     * @param ctx the ManagerContext to be used by the PrescriptionRepository
     */
    public PrescriptionRepository(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Creates a new Prescription with the specified medical name and quantity.
     *
     * @param medicalName the name of the medication
     * @param quantity the quantity of the medication
     * @return the created Prescription
     */
    public Prescription create(String medicalName, int quantity){
        Prescription prescription = new Prescription(medicalName, quantity);
        models.add(prescription);
        return prescription;
    }

    /**
     * Updates the dispensed status of the Prescription with the specified ID.
     *
     * @param id the ID of the Prescription
     * @param isDispensed the new dispensed status of the Prescription
     */
    public void updateStatus(String id, boolean isDispensed){
        Prescription update = get(id);
        update.setStatus(isDispensed);
    }
    
    /**
     * Retrieves the Prescription with the specified ID.
     *
     * @param id the ID of the Prescription
     * @return the Prescription with the specified ID, or null if not found
     */
    @Override
    public Prescription get(String id) {
        for(Prescription prescription : models){
            if(prescription.getId().equals(id)){
                return prescription;
            }
        }
        return null;
    }

    /**
     * Creates an empty Prescription model.
     *
     * @return a new empty Prescription
     */
    @Override
    public Prescription createEmptyModel() {
        return new Prescription();
    }
}