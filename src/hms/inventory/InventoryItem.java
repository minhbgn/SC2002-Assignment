package hms.inventory;

import hms.common.IModel;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * InventoryItem class is a model class that represents an item in the inventory.
 */
public class InventoryItem implements IModel {
    /** Medical name of the item, equivalent to the id of the item */
    private String medicalName;
    /** Number of item left in stock */
    private int stock;
    /** Number below which will a warning of low stock will be raised */
    private int lowStock;
    /** Status of the item, true if the item is being requested, false if not */
    private boolean requested = false;
    /** Number of item requested by Pharmacists */
    private int num_requested;

    /**
     * Default constructor for InventoryItem class. 
     * It is used to be hydrated later
     */
    public InventoryItem() {
    }

    /**
     * Constructor for InventoryItem
     * @param medicalName Name of the item
     * @param stock Number of stock left in inventory
     * @param lowStock Number below which will a warning of low stock will be raised
     */
    public InventoryItem(String medicalName, int stock, int lowStock) {
        this.medicalName = medicalName;
        this.stock = stock;
        this.lowStock = lowStock;
    }

    public String getMedicalName() { return medicalName; }
    public int getStock() { return stock; }
    public int getRequestedAmount() { return num_requested; }
    public boolean isLowStock() { return stock < lowStock; }
    public int getLowStock() { return lowStock; }
    public boolean isRequested() { return requested; }

    public void setStock(int stock) { this.stock = stock; }
    public void setRequestedAmount(int requested_amount) { this.num_requested = requested_amount; }
    public void setLowStock(int lowStock) { this.lowStock = lowStock; }
    public void setRequested(boolean requested) { this.requested = requested; }
    
    @Override
    public String toString(){
        return "[InventoryItem]\n"
        		+ "medicine name: \t" + medicalName 
        		+ "\nstock: \t" + stock
        		+ "\nlow stock warning: " + lowStock
        		+ "\nrequested: " + (requested ? "yes":"no");
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
    	medicalName = data.get("medicalName");
    	stock = Integer.parseInt(data.get("stock"));
    	lowStock = Integer.parseInt(data.get("lowStock"));
    	requested = Boolean.parseBoolean(data.get("requested"));
    }

    @Override
    public HashMap<String, String> serialize() {
    	HashMap<String, String> data = new LinkedHashMap<>();
    	data.put("medicalName", medicalName);
    	data.put("stock", String.valueOf(stock));
    	data.put("lowStock", String.valueOf(lowStock));
    	data.put("requested", String.valueOf(requested));
    	return data;
    }    
}
