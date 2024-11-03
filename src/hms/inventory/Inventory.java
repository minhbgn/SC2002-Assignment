package hms.inventory;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;
import java.util.*;

public class Inventory extends AbstractRepository<InventoryItem> {
    public Inventory(ManagerContext ctx) {
        super(ctx);
    }

    public InventoryItem create(String medicalName, int stock, int lowStock){
    	HashMap<String, String> data = new HashMap<>();
    	data.put("medicalName", medicalName);
    	data.put("stock", String.valueOf(stock));
    	data.put("lowStock", String.valueOf(lowStock));
    	super.parse(data);
    	return get(medicalName);
    }

    public void updateStock(String medicalName, int stock){
    	InventoryItem update = get(medicalName);
    	update.setStock(stock);
    }

    public void updateRequest(String medicalName, boolean requested){
        InventoryItem update = get(medicalName);
        update.setRequested(requested);
    }

    @Override
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
    public InventoryItem createEmptyModel() {
    	return new InventoryItem();
    }
}
