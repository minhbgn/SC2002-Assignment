package hms.user.model;

import hms.appointment.Appointment;
import hms.appointment.AppointmentRecord;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.common.id.IdManager;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Doctor extends User {
	/**
	 * Empty constructor to be used for hydration
	 * @param ctx The manager context to let this User access other classes
	 */
    public Doctor(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Filled constructor for Doctor
     * @param ctx The manager context to let this User access other classes
     * @param name Name of the Doctor
     * @param isMale Gender of the Doctor, true if Male, false otherwise
     * @param contact Contact number of the Doctor
     * @param dob Day of birth of the Doctor
     */
    public Doctor(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.account = new Account(IdManager.generateId(Doctor.class));
    }

	/**
	 * View all appointments under certain criteria
	 * @param criteria The criteria under which the appointment will be looked for
	 * @return The list of all appointments that satisfy the criteria
	 */
    public ArrayList<Appointment> viewAppointments(ArrayList<SearchCriterion<Appointment, ?>> criteria) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        return new ArrayList<>(manager.getAppointments(criteria));
    }

    /**
     * View all records whose appointment falls under certain criteria
     * @param criteria The criteria under which the appointment will be looked for
     * @return The list of records from all appointments satisfy all constraint
     */
    public ArrayList<AppointmentRecord> viewRecords(ArrayList<SearchCriterion<Appointment,?>> criteria){
    	ArrayList<Appointment> appointment_list = viewAppointments(criteria);
    	ArrayList<AppointmentRecord> records = new ArrayList<>();
    	for (Appointment appointment : appointment_list) {
    		records.add(appointment.getRecord());
    	}
    	return records;
    }

    /**
     * Accept or reject the pending appointment
     * @param appId ID of the appointment
     * @param accepted true if accepted, false if rejected
     */
    public void resolvePendingAppointment(String appId, boolean accepted) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        List<Appointment> appointments = manager.getAppointments(null);
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appId) && appointment.getStatus() == AppointmentStatus.PENDING) {
                manager.updateStatus(appId, accepted ? AppointmentStatus.ACCEPTED : AppointmentStatus.REJECTED);
            }
        }
    }

    /**
     * Finish an appointment
     * @param appId ID of the appointment
     * @param record The record which we want to finalize the appointment with. This record can be generated outside Appointment.
     */
    public void completeAppointment(String appId, AppointmentRecord record) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        List<Appointment> appointments = manager.getAppointments(null);
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appId)) {
                manager.updateStatus(appId, AppointmentStatus.FINISHED);
                manager.resolveAppoinment(appId, record.getService(), record.getPrescriptions(), record.getNotes());   
            }
        }
    }

    /**
     * Update an appointment record
     * @param appId ID of the appointment
     * @param service Type of services received by patient
     * @param prescriptionIds Prescriptions given to patient
     * @param notes Other remarks
     */
    public void updateAppointmentRecord(String appId, String service, ArrayList<String> prescriptionIds, String notes) {
    	AppointmentManager manager = ctx.getManager(AppointmentManager.class);
    	manager.resolveAppoinment(appId, service, prescriptionIds, notes);
    	manager.updateStatus(appId, AppointmentStatus.ACCEPTED);
    }
    
    @Override
    /**
     * To aid in printing out a Doctor object
     */
    public String toString() {
        return super.toString()+"\n User type: [Doctor]";
    }
}
