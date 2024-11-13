package hms.prescription;

import hms.common.IModel;
import hms.common.id.IdManager;
import java.util.HashMap;

public class Prescription implements IModel{
    private String id;
    private String medicalName;
    private boolean isDispensed = false;
    private int quantity;

    Prescription() {
    }

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

    public String getId() {
        return id;
    }

    public String getMedicalName() {
        return medicalName;
    }

    public boolean getStatus() {
        return isDispensed;
    }

    public void setStatus(boolean isDispensed) {
        this.isDispensed = isDispensed;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        this.id = data.get("id");
        this.isDispensed = Boolean.parseBoolean(data.get("isDispensed"));
        this.medicalName = data.get("medicalName");
        this.quantity = Integer.parseInt(data.get("quantity")); 
    }

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
