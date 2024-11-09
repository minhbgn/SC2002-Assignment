package hms.user.model;

import java.util.*;

import hms.appointment.*;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.prescription.Prescription;
import hms.manager.AppointmentManager;
import hms.manager.InventoryManager;
import hms.manager.PrescriptionManager;

public class Pharmacist extends User {
	/**
	 * Default constructor for Pharmacist.
	 * @param ctx Manager context to access other classes
	 */
    public Pharmacist(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Attribute-filled constructor for Pharmacist
     * @param ctx Manager context to access other classes
     * @param name Name of the Pharmacist
     * @param isMale Gender of the Pharmacist: True if Male, False otherwise
     * @param contact Contact number of the Pharmacist
     * @param dob Day of birth of the Pharmacist
     */
    public Pharmacist(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
    }

    /**
     * View all the current appointment records
     * @return the list of appointment records
     */
    public ArrayList<AppointmentRecord> viewAppointmentRecords() {
    	AppointmentManager manager = ctx.getManager(AppointmentManager.class);
    	ArrayList<AppointmentRecord> records_list = new ArrayList<>();
    	for (Appointment appointments : manager.getAppointments(null)) {
    		records_list.add(appointments.getRecord());
    	}
    	return records_list;
    }

    /**
     * Resolve the prescription by dispensing medicine
     * A message is printed to notify if the resolve is successful or not
     * @param prescriptionId The prescription id to identify different prescriptions
     */
    public void resolvePrescription (String prescriptionId) {
        PrescriptionManager manager = ctx.getManager(PrescriptionManager.class);
        if(manager.updatePrescription(prescriptionId, true)){
        	System.out.println("Prescription resolved successfully.");
        }
        else System.out.println("Prescription resolved unsuccessfully. Check medical name or"
        		+ " inventory levels");
    }

    /**
     * View the current prescription, by different criteria
     * @param criteria The criteria to find different prescriptions
     * @return The list of prescriptions that satisfy the criteria
     */
    public ArrayList<Prescription> viewPrescriptions(List<SearchCriterion<Prescription, ?>> criteria) {
        PrescriptionManager manager = ctx.getManager(PrescriptionManager.class);
        ArrayList <Prescription> prescription_list = new ArrayList<Prescription>(manager.getPrescriptions(criteria));
        return prescription_list;
    }

    /**
     * View the stock of a medical item
     * @param medicalName The name of the medical item
     */
    public void viewStock(String medicalName) {
        InventoryManager manager = ctx.getManager(InventoryManager.class);
        System.out.println(manager.viewInventoryItem(medicalName).getStock());
    }

    /**
     * Raise restock request for Admin
     * @param medicalName The name of the medical item that needs restock
     */
    public void requestRestock(String medicalName, int requested_amount) {
        InventoryManager manager = ctx.getManager(InventoryManager.class);
        if(manager.updateInventoryItemRequestStatus(medicalName, true, requested_amount)){
        	System.out.println("Item requested");
        }
        else {
        	System.out.println("Error! This item not existed in inventory yet!");
        }
    }

    @Override
    /**
     * Function to print out Pharmacist
     */
    public String toString() {
        return super.toString()+"\n User type: [Pharmacist]";
    }
}
