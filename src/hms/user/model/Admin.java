package hms.user.model;

import java.util.Date;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.inventory.InventoryItem;
import hms.manager.ManagerContext;

public class Admin extends User {
    public Admin(ManagerContext ctx) {
        super(ctx);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Admin(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public User[] viewAllStaff(SearchCriterion<User, ?>[] criteria) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Appointment[] viewAppointments(SearchCriterion<Appointment, ?>[] criteria) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public InventoryItem[] viewInventory(SearchCriterion<InventoryItem, ?>[] criteria) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public InventoryItem[] viewLowStock() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public InventoryItem[] viewRestockRequests() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
