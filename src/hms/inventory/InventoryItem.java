package hms.inventory;

import java.util.HashMap;
import java.util.*;

import hms.common.IModel;

public class InventoryItem implements IModel {
    private String medicalName; // Medical name of the item, equivalent to the id of the item
    private int stock;
    private int lowStock;
    private boolean requested = false;
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

    /**
     * Get the name of the item
     * @return Name of the item
     */
    public String getMedicalName() {
        return medicalName;
    }
    
    /**
     * Get the number of item left in stock
     * @return The number of item left in stock
     */
    public int getStock() {
        return stock;
    }
    
    /**
     * Get the number of item requested by Pharmacists
     * @return The number of item requested by Pharmacists
     */
    public int getRequestedAmount() {
    	return num_requested;
    }

    /**
     * Set or modify the number of item left in stock
     * @param stock New number of item left in stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    /**
     * Set or modify the number of item requested
     * @param requested_amount The number of items requested
     */
    public void setRequestedAmount(int requested_amount) {
    	this.num_requested = requested_amount;
    }

    /**
     * Check if stock is below the warning level
     * @return true if stock is lower than the lowStock warning level, false otherwise
     */
    public boolean isLowStock() {
        return stock < lowStock;
    }
    
    /**
     * Check the number below which the warning will be raised
     * @return The benchmark number
     */
    public int getLowStock() {
    	return lowStock;
    }
    
    /**
     * Set or modify the warning level, below which a warning will be raised
     * @param lowStock The new warning level of stock
     */
    public void setLowStock(int lowStock) {
    	this.lowStock = lowStock;
    }

    /**
     * Check if the item is currently requested by other users/objects
     * @return true if the item is requested, false otherwise
     */
    public boolean isRequested() {
        return requested;
    }

    /**
     * Set the requested status of the item, true if the item is being requested, false if not
     * @param requested The new requested status of the item
     */
    public void setRequested(boolean requested) {
        this.requested = requested;
    }
    
    @Override
    /**
     * To help in printing out the content of the object
     */
    public String toString(){
        // TODO Auto-generated method stub
        return "[InventoryItem]\n"
        		+ "medicine name: \t" + medicalName 
        		+ "\nstock: \t" + stock
        		+ "\nlow stock warning: " + lowStock;
    }

    @Override
    /**
     * Give the item object its neccessary information about its attribute from a HashMap. 
     * This method is used to easily read data from external files like csv and excel into the internal model. 
     * This method does the opposite function of serialize()
     * @param data The HashMap that contains the neccessary data of the item
     */
    public void hydrate(HashMap<String, String> data) {
    	medicalName = data.get("medicalName");
    	stock = Integer.parseInt(data.get("stock"));
    	lowStock = Integer.parseInt(data.get("lowStock"));
    	requested = (data.get("requested") == "true");
    }

    @Override
    /**
     * Generate a HashMap containing an item's neccessary information
     * This method is used to easily write the item's data back to the external files like csv and excel
     * This method does the opposite function of hydrate()
     * @return The HashMap that contains every important information
     */
    public HashMap<String, String> serialize() {
    	HashMap<String, String> data = new HashMap<>();
    	data.put("medicalName", medicalName);
    	data.put("stock", String.valueOf(stock));
    	data.put("lowStock", String.valueOf(lowStock));
    	data.put("requested", String.valueOf(requested));
    	return data;
    }    
}
