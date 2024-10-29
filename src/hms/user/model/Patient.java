package hms.user.model;

import java.util.Date;
import java.util.HashMap;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;

public class Patient extends User {
    public Patient(ManagerContext ctx) {
        super(ctx);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Patient(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void scheduleAppointment(String doctorId, Date date) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void rescheduleAppointment(String appId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void cancelAppointment(String appId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Appointment[] viewAppointments(SearchCriterion<Appointment, ?>[] criteria) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public HashMap<String, String> serialize() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
