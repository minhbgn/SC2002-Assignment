package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.AbstractRepository;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.List;

/** Repository class for managing appointments in the hospital management system. */
public class AppointmentRepository extends AbstractRepository<Appointment> {
    /**
     * Constructs a new AppointmentRepository with the specified ManagerContext.
     * @param ctx the ManagerContext to be used by the AppointmentRepository
     */
    public AppointmentRepository(ManagerContext ctx) { super(ctx); }

    /**
     * Creates a new Appointment with the specified patient ID, doctor ID, and date.
     * @param patientId the ID of the patient
     * @param doctorId the ID of the doctor
     * @param timeslot the timeslot of the appointment
     * @return the created Appointment
     */
    public Appointment create(String patientId, String doctorId, Timeslot timeslot) {
        Appointment appointment = new Appointment(patientId, doctorId, timeslot, AppointmentStatus.PENDING);
        models.add(appointment);
        return appointment;
    }

    /**
     * Updates the timeslot of the Appointment with the specified ID.
     * @param id the ID of the Appointment
     * @param timeslot the new timeslot
     */
    public void updateTimeslot(String id, Timeslot timeslot) { get(id).setTimeslot(timeslot); }

    /**
     * Updates the status of the Appointment with the specified ID.
     * @param id the ID of the Appointment
     * @param status the new status
     */
    public void updateStatus(String id, AppointmentStatus status) { get(id).setStatus(status); }

    /**
     * Updates the medical record of the Appointment with the specified ID.
     * @see Appointment#setRecord(String, ArrayList, String)
     * @param id the ID of the Appointment
     * @param service the service provided during the appointment
     * @param prescriptionIds the list of prescription IDs given during the appointment
     * @param notes additional notes about the appointment
     */
    public void updateRecord(String id, String service, ArrayList<String> prescriptionIds, String notes) {
        get(id).setRecord(service, prescriptionIds, notes);
    }

    @Override
    public Appointment get(String id) {
        return findWithFilters(List.of(new SearchCriterion<>(Appointment::getId, id))).get(0);
    }

    @Override
    public Appointment createEmptyModel() {
        return new Appointment();
    }
}