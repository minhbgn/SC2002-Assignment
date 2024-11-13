package hms.user.model;

import java.util.*;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.common.id.IdManager;
import hms.inventory.InventoryItem;
import hms.manager.*;

public class Admin extends User {
	/**
	 * Blank constructor for object to be hydrated later
	 * @param ctx The manager context to access other classes
	 */
    public Admin(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Filled constructor for the object
     * @param ctx The manager context to access other classes
     * @param name Name of Admin
     * @param isMale Gender of Admin: true if Male, false otherwise
     * @param contact Contact number of Admin
     * @param dob Day of birth of Admin
     */
    public Admin(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.account = new Account(IdManager.generateId(Admin.class));
    }

    /**
     * Used to view all staff (Admin, Doctor, Pharmacist) under certain criteria
     * @param criteria The criteria under which the user is searched for
     * @return The list of users who satisfy all criteria
     */
    public ArrayList<User> viewAllStaff(List<SearchCriterion<User, ?>> criteria) {
    	UserManager manager = ctx.getManager(UserManager.class);
    	ArrayList<User> staff_list = new ArrayList<>();
    	staff_list.addAll(manager.getUser(Admin.class, criteria));
    	staff_list.addAll(manager.getUser(Doctor.class, criteria));
    	staff_list.addAll(manager.getUser(Pharmacist.class,criteria));
    	return staff_list;
    }

    /**
     * Used to see all current appointments under certain criteria
     * @param criteria The criteria under which the appointment is searched for
     * @return The list of appointments who satisfy all criteria
     */
    public ArrayList<Appointment> viewAppointments(List<SearchCriterion<Appointment, ?>> criteria) {
    	AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        return new ArrayList<Appointment>(manager.getAppointments(criteria));
    }

    /**
     * Used to see all current items under certain criteria
     * @param criteria The criteria under which the item is searched for
     * @return The list of items who satisfy all criteria
     */
    public ArrayList<InventoryItem> viewInventory(List<SearchCriterion<InventoryItem, ?>> criteria) {
        InventoryManager manager = ctx.getManager(InventoryManager.class);
        return new ArrayList<InventoryItem>(manager.getInventoryItem(criteria));
    }

    /**
     * Used to see all current items that is low in stock
     * @return The list of items whose stock is below lowStock level
     */    
    public ArrayList<InventoryItem> viewLowStock() {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
        List<SearchCriterion<InventoryItem,?>> criteria = new ArrayList<>();
        SearchCriterion<InventoryItem, Boolean> lowStockCriterion =
        	    new SearchCriterion<>(InventoryItem::isLowStock, true);
        criteria.add(lowStockCriterion);
        return new ArrayList<InventoryItem>(manager.getInventoryItem(criteria));
    }

    /**
     * Used to see all requests raised by Pharmacists
     * @return The list of items requested by pharmacist
     */
    public ArrayList<InventoryItem> viewRestockRequests() {
        InventoryManager manager = ctx.getManager(InventoryManager.class);
        List<SearchCriterion<InventoryItem,?>> criteria = new ArrayList<>();
        SearchCriterion<InventoryItem, Boolean> restockRequested = 
        		new SearchCriterion<>(InventoryItem::isRequested,true);
        criteria.add(restockRequested);
        return new ArrayList<InventoryItem>(manager.getInventoryItem(criteria));
    }
 
    /**
     * Approve replenish requests of Pharmacist.
     * Item's stock will be added according to the request.
     * @param medicalName The name of the item requested
     */
    public void approveRestockRequests(String medicalName) {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
    	InventoryItem item = manager.viewInventoryItem(medicalName);
    	if (item != null && item.isRequested() == true) {
    		manager.updateInventoryItemStock(medicalName, item.getRequestedAmount());
    		manager.updateInventoryItemRequestStatus(medicalName, false, 0);
    	}
    }
    
    /**
     * Update stock manually
     * @param medicalName Name of the item whose stock number to be updated
     * @param changeStock Change in stock. Final stock number will be: afterStock = currentStock + changeStock
     */
    public void updateStock(String medicalName, int changeStock) {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
    	manager.updateInventoryItemStock(medicalName, changeStock);
    }
    
    /**
     * Update the lowStock benchmark, under which item will raise the isLowStock signal
     * @param medicalName Name of the item whose low stock benchmark number to be updated
     * @param newLowStock New benchmark number
     */
    public void updateLowStock(String medicalName, int newLowStock) {
    	InventoryManager manager = ctx.getManager(InventoryManager.class);
    	manager.updateInventoryItemLowStockThreshold(medicalName, newLowStock);
    }

    @Override
    /**
     * To aid in printing out the Admin object
     */
    public String toString() {
        return super.toString()+"\n User type: [Admin]";
    }
}
