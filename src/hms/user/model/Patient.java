package hms.user.model;
import hms.appointment.enums.AppointmentStatus;
import hms.appointment.*;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;

public class Patient extends User {
    private List<Appointment> appointments;

    public Patient(ManagerContext ctx) {
        super(ctx);
        this.appointments = new ArrayList<>();
    }

    public Patient(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.appointments = new ArrayList<>();
    }

    public void scheduleAppointment(String PatientId, String doctorId, Date date, AppointmentStatus status) {
    	AppointmentManager manager = ctx.getManager(AppointmentManager.class);
    	manager.makeAppointment(PatientId, doctorId, null);
        Appointment appointment = new Appointment(PatientId, doctorId, date, status);
        appointments.add(appointment);
    }

    public void rescheduleAppointment(String appId, Date newDate) {
    	AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        manager.updateDate(appId, newDate);
    }

    public void cancelAppointment(String appId) {
        appointments.removeIf(appointment -> appointment.getId().equals(appId));
    }

    public Appointment[] viewAppointments(SearchCriterion<Appointment, ?>[] criteria) {
        return appointments.toArray(new Appointment[0]);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + this.name + '\'' +
                ", isMale=" + this.isMale +
                ", contact='" + this.contact + '\'' +
                ", dob=" + this.dob +
                ", appointments=" + appointments +
                '}';
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        this.name = data.get("name");
        this.isMale = Boolean.parseBoolean(data.get("isMale"));
        this.contact = data.get("contact");
        this.dob = new Date(Long.parseLong(data.get("dob")));
    }

    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", this.name);
        data.put("isMale", Boolean.toString(this.isMale));
        data.put("contact", this.contact);
        data.put("dob", Long.toString(this.dob.getTime()));
        return data;
    }
}
