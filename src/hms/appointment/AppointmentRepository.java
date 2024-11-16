package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.AbstractRepository;
import hms.manager.ManagerContext;
import java.util.ArrayList;
import java.util.Date;

/**
 * Repository class for managing appointments in the hospital management system.
 */
public class AppointmentRepository extends AbstractRepository<Appointment> {

    /**
     * Constructs a new AppointmentRepository with the specified ManagerContext.
     *
     * @param ctx the ManagerContext to be used by the AppointmentRepository
     */
    public AppointmentRepository(ManagerContext ctx) {
        super(ctx);
    }

    /**
     * Creates a new Appointment with the specified patient ID, doctor ID, and date.
     *
     * @param patientId the ID of the patient
     * @param doctorId the ID of the doctor
     * @param date the date of the appointment
     * @return the created Appointment
     */
    public Appointment create(String patientId, String doctorId, Date date) {
        Appointment appointment = new Appointment(patientId, doctorId, date, AppointmentStatus.PENDING);
        models.add(appointment);
        return appointment;
    }

    /**
     * Updates the date of the Appointment with the specified ID.
     *
     * @param id the ID of the Appointment
     * @param date the new date of the Appointment
     */
    public void updateDate(String id, Date date) {
        Appointment appointment = get(id);
            appointment.setDate(date);
    }

    /**
     * Updates the status of the Appointment with the specified ID.
     *
     * @param id the ID of the Appointment
     * @param status the new status of the Appointment
     */
    public void updateStatus(String id, AppointmentStatus status) {
        Appointment appointment = get(id);
            appointment.setStatus(status);
    }

    /**
     * Updates the medical record of the Appointment with the specified ID.
     *
     * @param id the ID of the Appointment
     * @param service the service provided during the appointment
     * @param prescriptionIds the list of prescription IDs given during the appointment
     * @param notes additional notes about the appointment
     */
    public void updateRecord(String id, String service, ArrayList<String> prescriptionIds, String notes) {
        get(id).setRecord(service, prescriptionIds, notes);
    }

    /**
     * Retrieves the Appointment with the specified ID.
     *
     * @param id the ID of the Appointment
     * @return the Appointment with the specified ID, or throws an exception if not found
     */
    @Override
    public Appointment get(String id) {
        return models.stream()
        .filter(appointment -> appointment.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + id));
    }

    /**
     * Creates an empty Appointment model.
     *
     * @return a new empty Appointment
     */
    @Override
    public Appointment createEmptyModel() {
        return new Appointment();
    }
}