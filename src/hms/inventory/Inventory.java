package hms.inventory;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;
import java.util.*;

public class Inventory extends AbstractRepository<InventoryItem> {
	/**
	 * Constructor for Inventory
	 * Generate a repository (ArrayList) of type InventoryItem
	 * @param ctx The Manager Context: is used to let other subjects access this
	 */
    public Inventory(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Create an InventoryItem inside the Inventory
     * @param medicalName Name of the item
     * @param stock Number of stock left in inventory
     * @param lowStock Number of stock below which will raise a warning of lowStock
     * @return the item just created
     */
    public InventoryItem create(String medicalName, int stock, int lowStock){
    	HashMap<String, String> data = new HashMap<>();
    	data.put("medicalName", medicalName);
    	data.put("stock", String.valueOf(stock));
    	data.put("lowStock", String.valueOf(lowStock));
    	super.parse(data);
    	return get(medicalName);
    }
    
    /**
     * Update the number of stock inside the Inventory
     * @param medicalName Name of the item to be modified
     * @param stock New number of stock left in inventory
     */
    public void updateStock(String medicalName, int stock){
    	InventoryItem update = get(medicalName);
    	update.setStock(stock);
    }
    
    /**
     * Update the requested status of an item: true/false
     * @param medicalName Name of the item to be modified
     * @param requested New requested status of the item
     */
    public void updateRequest(String medicalName, boolean requested){
        InventoryItem update = get(medicalName);
        update.setRequested(requested);
    }

    /**
     * Update the new low stock warning level
     * @param medicalName Name of the item to be modified
     * @param lowStock New warning level of the item
     */
    public void updateLowStock(String medicalName, int lowStock) {
    	InventoryItem update = get(medicalName);
    	update.setLowStock(lowStock);
    }
    
    /**
     * Remove the item entirely from the inventory (Different than empty stock). This is used when
     * the hospital no longer wants this kind of item anymore
     * @param medicalName Name of the item to be removed
     */
    public void remove(String medicalName) {
    	InventoryItem remove = get(medicalName);
    	models.remove(remove);
    }
    
    @Override
    /**
     * Find the item inside inventory
     * @param id The id (name) of the item
     * @return the object instance of the item inside the inventory
     */
    public InventoryItem get(String id) {
        // TODO Auto-generated method stub
        for (InventoryItem result : super.models) {
        	if (result.getMedicalName() == id) {
        		return result;
        	}
        }
        System.out.println("Error: No medicine in stock with name: " + id);
        return createEmptyModel();
    }

    @Override
    /**
     * @return an empty item to be hydrated with data later
     */
    public InventoryItem createEmptyModel() {
    	return new InventoryItem();
    }
}
