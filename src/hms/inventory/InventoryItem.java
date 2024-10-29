package hms.inventory;

import java.util.HashMap;

import hms.common.IModel;

public class InventoryItem implements IModel {
    private String medicalName; // Medical name of the item, equivalent to the id of the item
    private int stock;
    private int lowStock;
    private boolean requested = false;

    public InventoryItem() {
    }

    public InventoryItem(String medicalName, int stock, int lowStock) {
        this.medicalName = medicalName;
        this.stock = stock;
        this.lowStock = lowStock;
    }

    public String getMedicalName() {
        return medicalName;
    }
    
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isLowStock() {
        return stock < lowStock;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    @Override
    public String toString(){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toString'");
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
