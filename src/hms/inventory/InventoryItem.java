package hms.inventory;

import java.util.HashMap;
import java.util.*;

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
    
    public int getLowStock() {
    	return lowStock;
    }
    
    public void setLowStock(int lowStock) {
    	this.lowStock = lowStock;
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
        return "[InventoryItem]\n"
        		+ "medicine name: \t" + medicalName 
        		+ "\nstock: \t" + stock
        		+ "\nlow stock warning: " + lowStock;
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
    	medicalName = data.get("medicalName");
    	stock = Integer.parseInt(data.get("stock"));
    	lowStock = Integer.parseInt(data.get("lowStock"));
    	requested = (data.get("requested") == "true");
    }

    @Override
    public HashMap<String, String> serialize() {
    	HashMap<String, String> data = new HashMap<>();
    	data.put("medicalName", medicalName);
    	data.put("stock", String.valueOf(stock));
    	data.put("lowStock", String.valueOf(lowStock));
    	data.put("requested", String.valueOf(requested));
    	return data;
    }    
}
