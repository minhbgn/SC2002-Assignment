package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.IModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
public class Appointment implements IModel{
    private String id;
    private String patientId;
    private String doctorId;
    private Date date;
    private AppointmentStatus status;
    private AppointmentRecord record = null;

    public Appointment() {
    }

    public Appointment(String patientId, String doctorId, Date date, AppointmentStatus status) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentRecord getRecord() {
        return record;
    }

    public void setRecord(String service, ArrayList<String> prescriptions, String notes) {
        // TODO Auto-generated method stub
        this.record = new AppointmentRecord(service, prescriptions, notes);
        this.status = AppointmentStatus.FINISHED;
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        this.id = data.get("id");
        this.patientId = data.get("patientId");
        this.doctorId = data.get("doctorId");
        this.date = new Date(Long.parseLong(data.get("date")));
        this.status = AppointmentStatus.valueOf(data.get("status"));
        if (data.get("service").equals("empty")) {
            this.record = null;
        } else {
            ArrayList<String> prescriptions = new ArrayList<>();
            for(String prescriptionId : data.get("prescriptions").split("/")) {
                prescriptions.add(prescriptionId);
            }
            this.record = new AppointmentRecord(data.get("service"), prescriptions, data.get("notes"));
        }
    }

    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("id", this.id);
        data.put("patientId", this.patientId);
        data.put("doctorId", this.doctorId);
        data.put("date", String.valueOf(this.date));
        data.put("status", this.status.name());
        if(this.record == null) {
            data.put("service", "empty");
            data.put("notes", "empty");
            data.put("prescriptions", "empty");
        }
        else {
            data.putAll(record.serialize());
        }
        return data;
    }
}
