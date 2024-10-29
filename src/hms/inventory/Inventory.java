package hms.inventory;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;

public class Inventory extends AbstractRepository<InventoryItem> {
    public Inventory(ManagerContext ctx) {
        super(ctx);
    }

    public InventoryItem create(String medicalName, int stock, int lowStock){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateStock(String medicalName, int stock){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateRequest(String medicalName, boolean requested){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InventoryItem get(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public InventoryItem createEmptyModel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyModel'");
    }
}
