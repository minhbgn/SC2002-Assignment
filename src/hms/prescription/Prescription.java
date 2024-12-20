package hms.prescription;

import hms.common.IModel;
import hms.common.id.IdManager;
import hms.common.id.IdParser;
import hms.common.id.IdRegistry;
import java.util.HashMap;
import java.util.LinkedHashMap;

/** Represents a prescription in the hospital management system. */
public class Prescription implements IModel {
    /** The unique ID of the prescription. Automatically generated by IdManager. */
    private String id;
    /** The name of the medication in the prescription. */
    private String medicalName;
    /** The dispensed status of the prescription. */
    private boolean isDispensed = false;
    /** The quantity of the medication in the prescription. */
    private int quantity;

    /** Default constructor for the Prescription class. Used for hydration. */
    Prescription() { }
    /**
     * Constructs a new Prescription with the specified medical name and quantity.
     * The status of the Prescription is set to false by default.
     * @param medicalName the name of the medication
     * @param quantity the quantity of the medication
     */
    Prescription(String medicalName, int quantity) {
        this.medicalName = medicalName;
        this.quantity = quantity;
        this.id = IdManager.generateId(Prescription.class);
        this.isDispensed = false;
    }

    @Override
    public String toString() {
        return "[Prescription]\n"
            + "ID: " + id + "\n"
            + "Medical Name: " + medicalName + "\n"
            + "Quantity: " + quantity + "\n"
            + "Status: " + (isDispensed ? "Dispensed" : "Not Dispensed") + "\n";
    }
    public String getId() { return id; }
    public String getMedicalName() { return medicalName; }
    public boolean getStatus() { return isDispensed; }
    public int getQuantity() { return quantity; }

    public void setStatus(boolean isDispensed) { this.isDispensed = isDispensed; }

    @Override
    public void hydrate(HashMap<String, String> data) {
        this.id = data.get("id");
        this.isDispensed = Boolean.parseBoolean(data.get("isDispensed"));
        this.medicalName = data.get("medicalName");
        this.quantity = Integer.parseInt(data.get("quantity")); 
        
        IdRegistry.tryUpdateId(Prescription.class, IdParser.getIdSuffix(id));
    }

    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new LinkedHashMap<>();
        data.put("id", id); 
        data.put("isDispensed", String.valueOf(isDispensed));
        data.put("medicalName", medicalName);
        data.put("quantity", String.valueOf(quantity));

        return data;
    }
}