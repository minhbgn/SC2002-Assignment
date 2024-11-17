package hms.prescription;

import hms.common.IModel;
import hms.common.id.IdManager;
import java.util.HashMap;

/**
 * Represents a prescription in the hospital management system.
 */
public class Prescription implements IModel {
    private String id;
    private String medicalName;
    private boolean isDispensed = false;
    private int quantity;

    /**
     * Default constructor for Prescription.
     */
    Prescription() {
    }

    /**
     * Constructs a new Prescription with the specified medical name and quantity.
     *
     * @param medicalName the name of the medication
     * @param quantity the quantity of the medication
     */
    Prescription(String medicalName, int quantity) {
        this.medicalName = medicalName;
        this.quantity = quantity;
        this.id = IdManager.generateId(Prescription.class);
        this.isDispensed = false;
    }

    /**
     * Returns a string representation of the Prescription.
     *
     * @return a string representation of the Prescription
     */
    @Override
    public String toString() {
        return "[Prescription]\n"
            + "ID: " + id + "\n"
            + "Medical Name: " + medicalName + "\n"
            + "Quantity: " + quantity + "\n"
            + "Status: " + (isDispensed ? "Dispensed" : "Not Dispensed") + "\n";
    }

    /**
     * Gets the ID of the Prescription.
     *
     * @return the ID of the Prescription
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the medical name of the Prescription.
     *
     * @return the medical name of the Prescription
     */
    public String getMedicalName() {
        return medicalName;
    }

    /**
     * Gets the dispensed status of the Prescription.
     *
     * @return true if the Prescription is dispensed, false otherwise
     */
    public boolean getStatus() {
        return isDispensed;
    }

    /**
     * Sets the dispensed status of the Prescription.
     *
     * @param isDispensed the new dispensed status of the Prescription
     */
    public void setStatus(boolean isDispensed) {
        this.isDispensed = isDispensed;
    }

    /**
     * Gets the quantity of the medication in the Prescription.
     *
     * @return the quantity of the medication
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Populates the Prescription's fields with data from the provided HashMap.
     *
     * @param data a HashMap containing the data to populate the Prescription's fields
     */
    @Override
    public void hydrate(HashMap<String, String> data) {
        this.id = data.get("id");
        this.isDispensed = Boolean.parseBoolean(data.get("isDispensed"));
        this.medicalName = data.get("medicalName");
        this.quantity = Integer.parseInt(data.get("quantity")); 
    }

    /**
     * Serializes the Prescription's fields into a HashMap.
     *
     * @return a HashMap containing the serialized data of the Prescription
     */
    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("id", id); 
        data.put("isDispensed", String.valueOf(isDispensed));
        data.put("medicalName", medicalName);
        data.put("quantity", String.valueOf(quantity));

        return data;
    }
}