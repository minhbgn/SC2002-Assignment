package hms.manager;

import hms.common.SearchCriterion;
import hms.common.id.IdManager;
import hms.prescription.Prescription;
import hms.prescription.PrescriptionRepository;
import java.util.List;

/**
 * The PrescriptionManager class manages the prescriptions in the system.
 * It provides methods to create, update, and retrieve prescriptions.
 * The get methods can be used to retrieve prescriptions based on certain criteria.
 */
public class PrescriptionManager extends AbstractManager<PrescriptionRepository> {
    /** Whether the PrescriptionManager has been initialized. */
    private static boolean initialized = false;
    
    /**
     * Constructor for the PrescriptionManager.
     * @param ctx The ManagerContext.
     * @param filepath The filepath to the file.
     */
    public PrescriptionManager(ManagerContext ctx, String filepath) {
        super(ctx, filepath);

        repository = new PrescriptionRepository(ctx);
    }

    @Override
    public void initialize() {
        if (!initialized) {
            IdManager.registerClass(Prescription.class, "PR");
            super.initialize();
            initialized = true;
        }
    }

    /**
     * Gets a list of prescriptions based on the search criteria.
     * @param criteria The search criteria.
     * @return A list of prescriptions.
     */
    public List<Prescription> getPrescriptions(List<SearchCriterion<Prescription,?>> criteria){
        return repository.findWithFilters(criteria);
    }

    /**
     * Checks if a prescription exists.
     * @param id The ID of the prescription.
     * @return True if the prescription exists, false otherwise.
     */
    public boolean hasPrescription(String id){
        return repository.get(id) != null;
    }

    /**
     * Creates a prescription. The prescription will not be created if:
     * - The medicalName does not exist in the inventory.
     * - The quantity is less than or equal to 0.
     * @param medicalName The name of the medical item.
     * @param quantity The quantity of the item.
     * @return The created prescription, or null if the prescription could not be created.
     */
    public Prescription createPrescription(String medicalName, int quantity){
        // Note: Current implementation requires the medicalName to exist in the inventory
        // However, this is not a strict requirement. The prescription can be created
        // even if the medicalName does not exist in the inventory. This reflects the situation
        // in real life where a doctor can prescribe a medicine that is not available
        // in the hospital's inventory but can be procured from an external source.
        if(!ctx.getManager(InventoryManager.class).hasInventoryItem(medicalName)) return null;

        if(quantity <= 0) return null;

        return repository.create(medicalName, quantity);
    }

    /**
     * Updates the prescription status. The status will not be updated if:
     * - The prescription does not exist.
     * - The stock of the item could not be updated (e.g., due to insufficient stock).
     * @param id The ID of the prescription.
     * @param isDispensed The status of the prescription.
     * @return True if the status was updated, false otherwise.
     */
    public boolean updatePrescription(String id, boolean isDispensed){
        if(!hasPrescription(id)) return false;

        Prescription prescription = repository.get(id);

        InventoryManager inventoryManager = ctx.getManager(InventoryManager.class);
        if (!inventoryManager.updateInventoryItemStock(prescription.getMedicalName(), prescription.getQuantity())) {
            return false;
        }

        repository.updateStatus(id, isDispensed);
        return true;
    }
}
