package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.IModel;
import hms.common.id.IdManager;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.HashMap;

/** Represents an appointment in the hospital management system. */
public class Appointment implements IModel {
    /** The unique ID of the appointment. Automatically generated by IdManager. */
    private String id;
    /** The ID of the patient who has the appointment. */
    private String patientId;
    /** The ID of the doctor who has the appointment. */
    private String doctorId;
    /** The timeslot of the appointment. */
    private Timeslot timeslot;
    /** The current status of the appointment. */
    private AppointmentStatus status;
    /** The medical record of the appointment. Only present if the status is FINISHED. */
    private AppointmentRecord record = null;

    /** Default constructor for the Appointment class. Used for hydration. */
    public Appointment() {}
    
    /**
     * Creates a new Appointment with the provided data.
     *
     * @param patientId the ID of the patient
     * @param doctorId the ID of the doctor
     * @param timeslot the timeslot of the appointment
     * @param status the status of the appointment
     */
    public Appointment(String patientId, String doctorId, Timeslot timeslot, AppointmentStatus status) {
    	this.id = IdManager.generateId(Appointment.class);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.timeslot = timeslot;
        this.status = status;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public Timeslot getTimeslot() { return timeslot; }
    public AppointmentStatus getStatus() { return status; }
    public AppointmentRecord getRecord() { return record; }

    void setStatus(AppointmentStatus status) { this.status = status; }
    public void setTimeslot(Timeslot timeslot) { this.timeslot = timeslot; }
    /**
     * Sets the medical record of the Appointment by instantiating a new AppointmentRecord
     * with the provided data.
     * @param service the service provided during the appointment
     * @param prescriptions the list of prescriptions given during the appointment
     * @param notes additional notes about the appointment
     */
    void setRecord(String service, ArrayList<String> prescriptions, String notes) {
        this.record = new AppointmentRecord(service, prescriptions, notes);
    }

    @Override
    public String toString() {
        return String.format(
            "Appointment %s from %s to %s (Status: %s)",
            id, timeslot.getStartTimeString(), timeslot.getEndTimeString(), status.name()
            );
        }
        
    @Override
    public void hydrate(HashMap<String, String> data) {
        this.id = data.get("id");
        this.patientId = data.get("patientId");
        this.doctorId = data.get("doctorId");        
        this.timeslot = new Timeslot(data.get("timeslot"));

        this.status = AppointmentStatus.valueOf(data.get("status"));
        if (data.get("status").equals("empty")) {
            this.record = null;
        } else {
            this.record = new AppointmentRecord();
            this.record.hydrate(data);
        }
    }

    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("id", this.id);
        data.put("patientId", this.patientId);
        data.put("doctorId", this.doctorId);
        data.put("timeslot", this.timeslot.toString());
        
        data.put("status", this.status.name());
        if(this.record == null) {
            data.put("service", "n/a");
            data.put("notes", "n/a");
            data.put("prescriptions", "n/a");
        }
        else {
            data.putAll(record.serialize());
        }
        return data;
    }
}