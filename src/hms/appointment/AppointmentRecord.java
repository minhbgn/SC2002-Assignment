package hms.appointment;

import hms.common.IModel;
import java.util.ArrayList;
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
        // TODO Auto-generated method stub
        if (data.containsKey("service")) {
            this.service = data.get("service");
        }
        if (data.containsKey("notes")) {
            this.notes = data.get("notes");
        }
        if (data.containsKey("prescriptionIds")) {
            String[] prescriptionIds = data.get("prescriptionIds").split("/");
            this.prescriptionIds = new ArrayList<>();
            for (String id : prescriptionIds) {
                this.prescriptionIds.add(id);
            }
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
