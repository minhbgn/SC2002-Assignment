package hms.appointment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import hms.appointment.enums.AppointmentStatus;
import hms.common.IModel;
import hms.prescription.Prescription;

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

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentRecord getRecord() {
        return record;
    }

    public void setRecord(AppointmentRecord record) {
        this.record = record;
    }

    public void resolve(String service, ArrayList<Prescription> prescriptions, String notes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resolve'");
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hydrate'");
    }

    @Override
    public HashMap<String, String> serialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }
}
