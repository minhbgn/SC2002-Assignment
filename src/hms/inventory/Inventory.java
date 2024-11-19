package hms.inventory;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;

/** Repository class for managing inventory items in the hospital management system. */
public class Inventory extends AbstractRepository<InventoryItem> {
	/**
     * Constructs a new Inventory with the specified ManagerContext.
     * @param ctx the ManagerContext to be used by the Inventory
	 */
    public Inventory(ManagerContext ctx) { super(ctx); }

    /**
     * Create an InventoryItem inside the Inventory
     * @param medicalName Name of the item
     * @param stock Number of stock left in inventory
     * @param lowStock Number of stock below which will raise a warning of lowStock
     * @return the item just created
     */
    public InventoryItem create(String medicalName, int stock, int lowStock){
        InventoryItem item = new InventoryItem(medicalName, stock, lowStock);
        models.add(item);
        return item;
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
     * @param requested_amount The amount of item requested
     */
    public void updateRequest(String medicalName, boolean requested, int requested_amount){
        InventoryItem update = get(medicalName);
        update.setRequested(requested);
        update.setRequestedAmount(requested_amount);
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
    public InventoryItem get(String id) {
        for (InventoryItem result : super.models) {
        	if (result.getMedicalName().equals(id)) {
        		return result;
        	}
        }
        System.out.println("Error: No medicine in stock with name: " + id);
        return createEmptyModel();
    }

    @Override
    public InventoryItem createEmptyModel() {
    	return new InventoryItem();
    }
}
