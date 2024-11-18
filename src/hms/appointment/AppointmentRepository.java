package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.AbstractRepository;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing appointments in the hospital management system.
 */
public class AppointmentRepository extends AbstractRepository<Appointment> {
    /**
     * Constructs a new AppointmentRepository with the specified ManagerContext.
     *
     * @param ctx the ManagerContext to be used by the AppointmentRepository
     */
    public AppointmentRepository(ManagerContext ctx) { super(ctx); }

    /**
     * Creates a new Appointment with the specified patient ID, doctor ID, and date.
     *
     * @param patientId the ID of the patient
     * @param doctorId the ID of the doctor
     * @param timeslot the time slot of the appointment
     * @return the created Appointment
     */
    public Appointment create(String patientId, String doctorId, Timeslot timeslot) {
        Appointment appointment = new Appointment(patientId, doctorId, timeslot, AppointmentStatus.PENDING);
        models.add(appointment);
        return appointment;
    }

    /**
     * Update the time slot of an appointment to a new date and time
     * @param id The id of the appointment whose time slot will be changed
     * @param timeslot The new time slot which the appointment will be set to
     */
    public void updateTimeslot(String id, Timeslot timeslot) { get(id).setTimeslot(timeslot); }

    /**
     * Update the status of an appointment
     * @param id The id of the appointment whose status will be updated
     * @param status The new status of the appointment
     */
    public void updateStatus(String id, AppointmentStatus status) { get(id).setStatus(status); }

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
        return findWithFilters(List.of(new SearchCriterion<>(Appointment::getId, id))).get(0);
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