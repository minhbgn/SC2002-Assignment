package hms.appointment;

import hms.common.IModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AppointmentRecord implements IModel {
    private String service;
    private ArrayList<String> prescriptionIds;
    private String notes;

    AppointmentRecord() {
    }

    AppointmentRecord(String service, ArrayList<String> prescriptionIds, String notes) {
        this.service = service;
        this.prescriptionIds = prescriptionIds;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AppointmentRecord{" +
            "service='" + service + '\'' +
            ", prescriptions=" + prescriptionIds +
            ", notes='" + notes + '\'' +
            '}';
    }

    public String getService() {
        return service;
    }

    public ArrayList<String> getPrescriptions() {
        return prescriptionIds;
    }

    public String getNotes() {
        return notes;
    }

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
