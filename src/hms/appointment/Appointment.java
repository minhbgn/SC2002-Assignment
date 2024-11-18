package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.IModel;
import hms.common.id.IdManager;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents an appointment in the hospital management system.
 */
public class Appointment implements IModel {
    private String id;
    private String patientId;
    private String doctorId;
    private Timeslot timeslot;
    private AppointmentStatus status;
    private AppointmentRecord record = null;

    /**
     * An empty constructor for the Appointment class
     * It is used to be hydrated later
     */
    public Appointment() {
    }

    /**
     * A fully filled constructor for the Appointment class
     * @param patientId the ID of the patient
     * @param doctorId the ID of the doctor
     * @param timeslot the time the Appointment takes place
     * @param status the status of the Appointment: accepted/rejected/cancelled/finished/pending.
     */
    public Appointment(String patientId, String doctorId, Timeslot timeslot, AppointmentStatus status) {
    	this.id = IdManager.generateId(Appointment.class);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.timeslot = timeslot;
        this.status = status;
    }

    /**
     * Get the id of the appointment
     * @return the id of the appointment
     */
    public String getId() {
        return id;
    }

    /**
     * Get the id of the patient attending this appointment
     * @return the id of the patient
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Get the id of the doctor attending this appointment
     * @return the id of the doctor
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Get the time the appointment takes place
     * @return the time the appointment takes place
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Get the status of the appointment
     * @return accepted/rejected/pending/cancelled/finished depends on the progress of the appointment
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Get the appointment record consisting of basic patient's information and medical outcomes
     * @return the appointment record
     */
    public AppointmentRecord getRecord() {
        return record;
    }

    /**
     * Sets the status of the Appointment.
     *
     * @param status the new status of the Appointment
     */
    void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Set the timeslot for the appointment
     * @param timeslot The timeslot of the appointment
     */
    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    /**
     * Sets the medical record of the Appointment.
     *
     * @param service the service provided during the appointment
     * @param prescriptions the list of prescriptions given during the appointment
     * @param notes additional notes about the appointment
     */
    void setRecord(String service, ArrayList<String> prescriptions, String notes) {
        this.record = new AppointmentRecord(service, prescriptions, notes);
        this.status = AppointmentStatus.FINISHED;
    }

 
    @Override
    /**
     * Method to aid in printing out the appointment's information
     */
    public String toString() {
        return String.format(
            "Appointment %s from %s to %s (Status: %s)",
            id, timeslot.getStartTimeString(), timeslot.getEndTimeString(), status.name()
        );
    }

    @Override
    /**
     * Populates the Appointment's fields with data from the provided HashMap.
     *
     * @param data a HashMap containing the data to populate the Appointment's fields
     */
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

    /**
     * Serializes the Appointment's fields into a HashMap.
     *
     * @return a HashMap containing the serialized data of the Appointment
     */
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