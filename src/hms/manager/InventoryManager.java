package hms.manager;

import hms.inventory.*;

/**
 * Manager class for the Inventory.
 */
public class InventoryManager extends AbstractManager<Inventory> {
    /**
     * Constructor for the InventoryManager.
     * @param ctx The ManagerContext.
     * @param filepath The filepath to the file.
     */
    public InventoryManager(ManagerContext ctx, String filepath) {
        super(ctx, filepath);
        repository = new Inventory(ctx);
    }

    /**
     * Checks if the inventory item exists.
     * @param medicalName The name of the medical item.
     * @return True if the item exists, false otherwise.
     */
    public boolean hasInventoryItem(String medicalName){
        return repository.get(medicalName) != null;
    }

    
    /**
     * Adds an inventory item. The item will not be added if:
     * - The item already exists.
     * - The quantity or low stock threshold is less than 0.
     * @param medicalName The name of the medical item.
     * @param quantity The quantity of the item.
     * @param lowStockThreshold The low stock threshold of the item.
     * @return True if the item was added, false otherwise.
     */
    public boolean addInventoryItem(String medicalName, int quantity, int lowStockThreshold){
        // Check if the item already exists
        if(hasInventoryItem(medicalName)){
            return false;
        }

        // Check if the quantity and low stock threshold are valid
        if(quantity < 0 || lowStockThreshold < 0){
            return false;
        }

        repository.create(medicalName, quantity, lowStockThreshold);
        return true;
    }

    /**
     * Access an item by calling its name
     * @param medicalName The name of the medical item
     * @return the medical item
     */
    public InventoryItem viewInventoryItem(String medicalName) {
    	if(hasInventoryItem(medicalName)) {
    		return repository.get(medicalName);
    	}
    	else {
    		System.out.println("Item does not exist");
    		return null;
    	}
    }
    
    /**
     * Updates the stock of an inventory item. The stock will not be updated if:
     * - The item does not exist.
     * - The resulting new stock is less than 0.
     * @param medicalName The name of the medical item.
     * @param change The change in stock.
     * @return True if the stock was updated, false otherwise.
     */
    public boolean updateInventoryItemStock(String medicalName, int change){
        // Check if the item exists
        if(!hasInventoryItem(medicalName)) return false;

        int newStock = repository.get(medicalName).getStock() + change;

        // Check if the new stock is valid
        if(newStock < 0) return false;

        repository.updateStock(medicalName, newStock);
        return true;
    }

    /**
     * Updates the low stock threshold of an inventory item. The low stock threshold will not be updated if:
     * - The item does not exist.
     * - The new low stock threshold is less than 0.
     * @param medicalName The name of the medical item.
     * @param lowStockThreshold The new low stock threshold.
     * @return True if the low stock threshold was updated, false otherwise.
     */
    public boolean updateInventoryItemLowStockThreshold(String medicalName, int lowStockThreshold){
        // Check if the item exists
        if(!hasInventoryItem(medicalName)) return false;
        
        // Check if the new low stock threshold is valid
        if(lowStockThreshold < 0) return false;

        repository.updateLowStock(medicalName, lowStockThreshold);
        return true;
    }

    /**
     * Updates the request status of an inventory item. The request status will not be updated if:
     * - The item does not exist.
     * @param medicalName The name of the medical item.
     * @param requested The new request status.
     * @return True if the request status was updated, false otherwise.
     */
    public boolean updateInventoryItemRequestStatus(String medicalName, boolean requested){
        // Check if the item exists
        if(!hasInventoryItem(medicalName)) return false;

        repository.updateRequest(medicalName, requested);
        return true;
    }
}
