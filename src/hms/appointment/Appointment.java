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

    public Appointment() {
    }

    public Appointment(String patientId, String doctorId, Timeslot timeslot, AppointmentStatus status) {
    	this.id = IdManager.generateId(Appointment.class);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.timeslot = timeslot;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

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

    /**
     * Populates the Appointment's fields with data from the provided HashMap.
     *
     * @param data a HashMap containing the data to populate the Appointment's fields
     */
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