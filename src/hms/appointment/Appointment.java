package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.IModel;
import hms.common.id.IdManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    	this.id = IdManager.generateId(Appointment.class);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public AppointmentRecord getRecord() {
        return record;
    }

    void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    void setDate(Date date) {
        this.date = date;
    }

    void setRecord(String service, ArrayList<String> prescriptions, String notes) {
        this.record = new AppointmentRecord(service, prescriptions, notes);
        this.status = AppointmentStatus.FINISHED;
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        this.id = data.get("id");
        this.patientId = data.get("patientId");
        this.doctorId = data.get("doctorId");
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            this.date = sdf.parse(data.get("date"));
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date in Appointment.hydrate");
        }

        this.status = AppointmentStatus.valueOf(data.get("status"));
        if (data.get("service").equals("empty")) {
            this.record = null;
        } else {
            ArrayList<String> prescriptions = new ArrayList<>();
            prescriptions.addAll(Arrays.asList(data.get("prescriptions").split("/")));
            this.record = new AppointmentRecord(data.get("service"), prescriptions, data.get("notes"));
        }
    }

    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("id", this.id);
        data.put("patientId", this.patientId);
        data.put("doctorId", this.doctorId);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        data.put("date", sdf.format(this.date));
        
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
