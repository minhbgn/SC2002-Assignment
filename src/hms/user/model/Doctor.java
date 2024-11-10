package hms.user.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import hms.App;
import hms.appointment.Appointment;
import hms.appointment.AppointmentRecord;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;

public class Doctor extends User {
    public Doctor(ManagerContext ctx) {
        super(ctx);
    }

    public Doctor(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
    }

    public Appointment[] viewAppointments(SearchCriterion<Appointment, ?>[] criteria) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        List<Appointment> appointments = manager.getAppointments(Arrays.asList(criteria));
    	return appointments.toArray(new Appointment[appointments.size()]);
    }

    public void resolvePendingAppointment(String appId, boolean accepted) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        List<Appointment> appointments = manager.getAppointments(null);
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appId) && appointment.getStatus() == AppointmentStatus.PENDING) {
                manager.updateStatus(appId, accepted ? AppointmentStatus.ACCEPTED : AppointmentStatus.REJECTED);
            }
        }
    }

    public void completeAppointment(String appId, AppointmentRecord record) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        List<Appointment> appointments = manager.getAppointments(null);
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appId)) {
                manager.updateStatus(appId, AppointmentStatus.FINISHED);
                manager.resolveAppoinment(appId, record.getService(), record.getPrescriptions(), record.getNotes());   
            }
        }
    }

    @Override
    public String toString() {
        return super.toString()+"\n User type: [Doctor]";
    }
}
