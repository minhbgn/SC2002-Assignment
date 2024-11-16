package hms.user.model;

import hms.appointment.Appointment;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.common.id.IdManager;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a patient in the hospital management system.
 */
public class Patient extends User {
    // Extra info about patient's medical records
    public String bloodType = "None Specified";
    public String allergies = "None Specified";
    public String medicalHistory = "None Specified";
    public String currentMedication = "None Specified";

    private final List<Appointment> appointments;

    /**
     * Constructs a new Patient with the specified ManagerContext.
     *
     * @param ctx the ManagerContext to be used by the Patient
     */
    public Patient(ManagerContext ctx) {
        super(ctx);
        
        this.account = new Account(IdManager.generateId(Patient.class));
        this.appointments = new ArrayList<>();
    }

    /**
     * Constructs a new Patient with the specified details.
     *
     * @param ctx the ManagerContext to be used by the Patient
     * @param name the name of the Patient
     * @param isMale the gender of the Patient
     * @param contact the contact information of the Patient
     * @param dob the date of birth of the Patient
     */
    public Patient(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.appointments = new ArrayList<>();
    }

    /**
     * Schedules a new appointment for the Patient.
     *
     * @param PatientId the ID of the Patient
     * @param doctorId the ID of the doctor
     * @param date the date of the appointment
     * @param status the status of the appointment
     */
    public void scheduleAppointment(String PatientId, String doctorId, Date date, AppointmentStatus status) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        manager.makeAppointment(PatientId, doctorId, null);
        Appointment appointment = new Appointment(PatientId, doctorId, date, status);
        appointments.add(appointment);
    }

    /**
     * Reschedules an existing appointment for the Patient.
     *
     * @param appId the ID of the appointment
     * @param newDate the new date for the appointment
     */
    public void rescheduleAppointment(String appId, Date newDate) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        manager.updateDate(appId, newDate);
    }

    /**
     * Cancels an existing appointment for the Patient.
     *
     * @param appId the ID of the appointment
     */
    public void cancelAppointment(String appId) {
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        manager.updateStatus(appId, AppointmentStatus.CANCELLED);
        appointments.removeIf(appointment -> appointment.getId().equals(appId));
    }

    /**
     * Views the appointments of the Patient based on the specified criteria.
     *
     * @param criteria the search criteria to filter appointments
     * @return an array of appointments that match the criteria
     */
    public Appointment[] viewAppointments(SearchCriterion<Appointment, ?>[] criteria) {
        return appointments.toArray(Appointment[]::new);
    }

    /**
     * Returns a string representation of the Patient.
     *
     * @return a string representation of the Patient
     */
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
}
