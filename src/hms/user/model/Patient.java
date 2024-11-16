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

public class Patient extends User {
    // Extra info about patient's medical records
    public String bloodType = "None Specified";
    public String allergies = "None Specified";
    public String medicalHistory = "None Specified";
    public String currentMedication = "None Specified";

    private final List<Appointment> appointments;

    public Patient(ManagerContext ctx) {
        super(ctx);
        
        this.account = new Account(IdManager.generateId(Patient.class));
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
        AppointmentManager manager = ctx.getManager(AppointmentManager.class);
        manager.updateStatus(appId, AppointmentStatus.CANCELLED);
        appointments.removeIf(appointment -> appointment.getId().equals(appId));
    }

    public Appointment[] viewAppointments(SearchCriterion<Appointment, ?>[] criteria) {
        return appointments.toArray(Appointment[]::new);
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
}
