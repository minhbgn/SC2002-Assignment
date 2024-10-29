package hms.prescription;

import java.util.HashMap;

import hms.common.IModel;

public class Prescription implements IModel{
    private String id;
    private String medicalName;
    private boolean isDispensed = false;
    private int quantity;

    Prescription() {
    }

    Prescription(String medicalName, int quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hydrate'");
    }

    @Override
    public HashMap<String, String> serialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }
}
