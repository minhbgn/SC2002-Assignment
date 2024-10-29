package hms.user.model;

import java.util.Date;

import hms.appointment.Appointment;
import hms.appointment.AppointmentRecord;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;

public class Doctor extends User {
    public Doctor(ManagerContext ctx) {
        super(ctx);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Doctor(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Appointment[] viewAppointments(SearchCriterion<Appointment, ?>[] criteria) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void resolvePendingAppointment(String appId, boolean accepted) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void completeAppointment(String appId, AppointmentRecord record) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
