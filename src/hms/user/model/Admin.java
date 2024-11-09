package hms.user.model;

import java.util.*;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.inventory.InventoryItem;
import hms.manager.*;

public class Admin extends User {
    public Admin(ManagerContext ctx) {
        super(ctx);
    }

    public Admin(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
    }

    public ArrayList<User> viewAllStaff(SearchCriterion<User, ?>[] criteria) {
    	throw new UnsupportedOperationException("Not implemented yet");
    }

    public ArrayList<Appointment> viewAppointments(List<SearchCriterion<Appointment, ?>> criteria) {
    	AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        return new ArrayList<Appointment>(manager.getAppointments(criteria));
    }

    public ArrayList<InventoryItem> viewInventory(List<SearchCriterion<InventoryItem, ?>> criteria) {
        InventoryManager manager = ctx.getManager(InventoryManager.class);
        return new ArrayList<InventoryItem>(manager.getInventoryItem(criteria));
    }

    public ArrayList<InventoryItem> viewLowStock() {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
        List<SearchCriterion<InventoryItem,?>> criteria = new ArrayList<>();
        SearchCriterion<InventoryItem, Boolean> lowStockCriterion =
        	    new SearchCriterion<>(InventoryItem::isLowStock, true);
        criteria.add(lowStockCriterion);
        return new ArrayList<InventoryItem>(manager.getInventoryItem(criteria));
    }

    public ArrayList<InventoryItem> viewRestockRequests() {
        InventoryManager manager = ctx.getManager(InventoryManager.class);
        List<SearchCriterion<InventoryItem,?>> criteria = new ArrayList<>();
        SearchCriterion<InventoryItem, Boolean> restockRequested = 
        		new SearchCriterion<>(InventoryItem::isRequested,true);
        criteria.add(restockRequested);
        return new ArrayList<InventoryItem>(manager.getInventoryItem(criteria));
    }
    
    public void approveRestockRequests(String medicalName) {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
    	InventoryItem item = manager.viewInventoryItem(medicalName);
    	if (item != null && item.isRequested() == true) {
    		manager.updateInventoryItemStock(medicalName, item.getRequestedAmount());
    		manager.updateInventoryItemRequestStatus(medicalName, false, 0);
    	}
    }
    
    public void updateStock(String medicalName, int newStock) {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
    	manager.updateInventoryItemStock(medicalName, newStock);
    }
    
    public void updateLowStock(String medicalName, int newLowStock) {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
    	manager.updateInventoryItemLowStockThreshold(medicalName, newLowStock);
    }

    @Override
    public String toString() {
        return super.toString()+"\n User type: [Admin]";
    }
}
