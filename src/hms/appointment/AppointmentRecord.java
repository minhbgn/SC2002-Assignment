package hms.appointment;

import hms.common.IModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a record of an appointment in the hospital management system.
 */
public class AppointmentRecord implements IModel {
    private String service;
    private ArrayList<String> prescriptionIds;
    private String notes;

    /**
     * Default constructor for AppointmentRecord.
     */
    AppointmentRecord() {
    }

    /**
     * Constructs a new AppointmentRecord with the specified service, prescription IDs, and notes.
     *
     * @param service the service provided during the appointment
     * @param prescriptionIds the list of prescription IDs given during the appointment
     * @param notes additional notes about the appointment
     */
    AppointmentRecord(String service, ArrayList<String> prescriptionIds, String notes) {
        this.service = service;
        this.prescriptionIds = prescriptionIds;
        this.notes = notes;
    }

    /**
     * Returns a string representation of the AppointmentRecord.
     *
     * @return a string representation of the AppointmentRecord
     */
    @Override
    public String toString() {
        return "AppointmentRecord{" +
            "service='" + service + '\'' +
            ", prescriptions=" + prescriptionIds +
            ", notes='" + notes + '\'' +
            '}';
    }

    /**
     * Gets the service provided during the appointment.
     *
     * @return the service provided during the appointment
     */
    public String getService() {
        return service;
    }

    /**
     * Gets the list of prescription IDs given during the appointment.
     *
     * @return the list of prescription IDs
     */
    public ArrayList<String> getPrescriptions() {
        return prescriptionIds;
    }

    /**
     * Gets additional notes about the appointment.
     *
     * @return additional notes about the appointment
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Populates the AppointmentRecord's fields with data from the provided HashMap.
     *
     * @param data a HashMap containing the data to populate the AppointmentRecord's fields
     */
    @Override
    public void hydrate(HashMap<String, String> data) {
        if (data.containsKey("service")) {
            this.service = data.get("service");
        }
        if (data.containsKey("notes")) {
            this.notes = data.get("notes");
        }
        if (data.containsKey("prescriptionIds")) {
            // If prescriptions is empty, set it to an empty list
            if (data.get("prescriptionIds").equals("")){
                this.prescriptionIds = new ArrayList<>();
                return;
            }

            String[] _prescriptionIds = data.get("prescriptionIds").split("/");
            this.prescriptionIds = new ArrayList<>();
            this.prescriptionIds.addAll(Arrays.asList(_prescriptionIds));
        }
    }

    /**
     * Serializes the AppointmentRecord's fields into a HashMap.
     *
     * @return a HashMap containing the serialized data of the AppointmentRecord
     */
    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("service", service);
        data.put("notes", notes);
        if (prescriptionIds != null) {
            data.put("prescriptionIds", String.join("/", prescriptionIds));
        }
        return data;
    }
}