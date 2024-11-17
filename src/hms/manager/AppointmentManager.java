package hms.manager;

import hms.appointment.Appointment;
import hms.appointment.AppointmentRepository;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.common.id.IdManager;
import hms.user.repository.DoctorRepository;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages the appointments in the system.
 */
public class AppointmentManager extends AbstractManager<AppointmentRepository> {
    private static boolean initialized = false;
    
    /**
     * Constructor for the AppointmentManager.
     * @param ctx The ManagerContext.
     * @param filepath The filepath to the file.
     */
    public AppointmentManager(ManagerContext ctx, String filepath){
        super(ctx, filepath);
        repository = new AppointmentRepository(ctx);
    }

    @Override
    public void initialize() {        
        if (!initialized) {
            IdManager.registerClass(Appointment.class, "AP");
            
            super.initialize();
            
            initialized = true;
        }
    }

    /**
     * Get all appointments that match the given criteria.
     * @param criteria The criteria to filter the appointments.
     * @return The list of appointments that match the criteria.
     */
    public List<Appointment> getAppointments(List<SearchCriterion<Appointment,?>> criteria){
        return repository.findWithFilters(criteria);
    }
    
    /**
     * Check if an appointment with the given ID exists.
     * @param id The ID of the appointment.
     * @return True if the appointment exists, false otherwise.
     */
    public boolean hasAppointment(String id) {
        return repository.get(id) != null;
    }

    /**
     * Check if a patient is free on the given timeslot.
     * @param patientId The ID of the patient.
     * @param timeslot The timeslot to check.
     * @return True if the patient is free, false otherwise.
     */
    public boolean isDoctorFree(String doctorId, Timeslot timeslot) {
        // Check if the doctor is free based on the appointments
        List<Appointment> appointments = getAppointments(List.of(
            new SearchCriterion<>(Appointment::getDoctorId, doctorId)
        ));

        for(Appointment appointment : appointments){
            if(appointment.getTimeslot().isOverlapping(timeslot)){
                return false;
            }
        }

        // Check if the doctor is free based on the busy timeslots
        List<Timeslot> busyTimeslots = ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .get(doctorId)
            .getBusyTimeslots();
        
        for(Timeslot busyTimeslot : busyTimeslots){
            if(busyTimeslot.isOverlapping(timeslot)){
                return false;
            }
        }

        return true;
    }

    /**
     * Check if a patient is free on the given timeslot.
     * @param patientId The ID of the patient.
     * @param timeslot The timeslot to check.
     * @return True if the patient is free, false otherwise.
     */
    public boolean isPatientFree(String patientId, Timeslot timeslot) {
        List<Appointment> appointments = getAppointments(List.of(
            new SearchCriterion<>(Appointment::getPatientId, patientId)
        ));

        for(Appointment appointment : appointments){
            if(appointment.getTimeslot().isOverlapping(timeslot)){
                return false;
            }
        }

        return true;
    }
    

    /**
     * Get all free timeslots for a patient and a doctor within a given timeslot.
     * @param patientId The ID of the patient.
     * @param doctorId The ID of the doctor.
     * @param timeslot The timeslot to check. It is assumed that the timeslot is within the working hours of the doctor.
     * @return An array of Timeslot objects representing the free timeslots. It is guaranteed that the timeslots intersect with the given timeslot.
     */
    public Timeslot[] getAllFreeTimeslots(String patientId, String doctorId, Timeslot timeslot) {
        List<Timeslot> unavailableTimeslots = new ArrayList<>();
        if(doctorId != null){
            // Doctor is busy during the working hours
            List<Appointment> appointments = getAppointments(List.of(
                new SearchCriterion<>(Appointment::getDoctorId, doctorId)
            ));

            // Remove appointments that are not accepted or pending
            appointments.removeIf(a -> a.getStatus() != AppointmentStatus.ACCEPTED &&
                a.getStatus() != AppointmentStatus.PENDING);

            unavailableTimeslots.addAll(appointments.stream()
                .map(Appointment::getTimeslot)
                .toList());

            // Doctor is also busy during the busy timeslots
            List<Timeslot> busyTimeslots = ctx.getManager(UserManager.class)
                .getRepository(DoctorRepository.class)
                .get(doctorId)
                .getBusyTimeslots();

            unavailableTimeslots.addAll(busyTimeslots);
        }

        if(patientId != null){
            // Patient is busy during their appointments
            List<Appointment> appointments = getAppointments(List.of(
                new SearchCriterion<>(Appointment::getPatientId, patientId)
            ));

            // Remove appointments that are not accepted or pending
            appointments.removeIf(a -> a.getStatus() != AppointmentStatus.ACCEPTED &&
                a.getStatus() != AppointmentStatus.PENDING);

            unavailableTimeslots.addAll(appointments.stream()
                .map(Appointment::getTimeslot)
                .toList());
        }

        // Get only relevant timeslots
        unavailableTimeslots.removeIf(t -> !t.isOverlapping(timeslot));

        // Initial free timeslot is the given timeslot
        List<Timeslot> freeTimeslots = new ArrayList<>();
        freeTimeslots.add(timeslot);

        // Note: This is a stupidly inefficient way to calculate free timeslots
        // but it is the simplest for now. It can be optimized later.
        // Every appointment will subtract its timeslot from the free timeslots
        for(Timeslot unavailableTimeslot : unavailableTimeslots){
            List<Timeslot> newFreeTimeslots = new ArrayList<>();
            for(Timeslot freeTimeslot : freeTimeslots){
                Timeslot[] subtractedTimeslots = Timeslot.subtract(freeTimeslot, unavailableTimeslot);
                
                newFreeTimeslots.addAll(Arrays.asList(subtractedTimeslots));
            }

            freeTimeslots = newFreeTimeslots;
        }

        return freeTimeslots.toArray(Timeslot[]::new);
    }

    /**
     * Create a new appointment. The appointment is created with the status PENDING.
     * @param patientId The ID of the patient.
     * @param doctorId The ID of the doctor.
     * @param date The date of the appointment.
     * @return The created appointment, or null if the patient or doctor does not exist.
     */
    public Appointment makeAppointment(String patientId, String doctorId, Timeslot timeslot){
        UserManager userManager = ctx.getManager(UserManager.class);

        if(!userManager.hasUser(patientId)) return null;
        if(!userManager.hasUser(doctorId)) return null;

        if(!isPatientFree(patientId, timeslot)) return null;
        if(!isDoctorFree(doctorId, timeslot)) return null;

        return repository.create(patientId, doctorId, timeslot);
    }

    /**
     * Finish an appointment. The appointment status is set to FINISHED.
     * If the prescription IDs are not valid (should not happen because of user input),
     * this method will return false.
     * @param id The ID of the appointment.
     * @param service The service provided during the appointment.
     * @param prescriptionIds The IDs of the prescriptions given during the appointment.
     * @param notes Additional notes about the appointment.
     * @return True if the appointment was successfully finished, false otherwise.
     */
    public boolean resolveAppoinment(String id, String service, ArrayList<String> prescriptionIds, String notes){
        if(!hasAppointment(id)) return false;

        PrescriptionManager prescriptionManager = ctx.getManager(PrescriptionManager.class);
        for(String prescriptionId : prescriptionIds){
            if(!prescriptionManager.hasPrescription(prescriptionId)){
                return false;
            }
        }

        repository.updateRecord(id, service, prescriptionIds, notes);
        return true;
    }

    /**
     * Update the date of an appointment. The status of the appointment will be set to PENDING.
     * @param id The ID of the appointment.
     * @param date The new date of the appointment.
     * @return True if the appointment was successfully updated, false otherwise.
     */
    public boolean updateTimeslot(String id, Timeslot timeslot){
        if(!hasAppointment(id)) return false;

        Appointment appointment = repository.get(id);

        // Check if the appointment status is valid for updating the date
        if(appointment.getStatus() == AppointmentStatus.CANCELLED
            || appointment.getStatus() == AppointmentStatus.FINISHED
            || appointment.getStatus() == AppointmentStatus.REJECTED){
            return false;
        }

        if(!isPatientFree(appointment.getPatientId(), timeslot)) return false;
        if(!isDoctorFree(appointment.getDoctorId(), timeslot)) return false;

        repository.updateStatus(id, AppointmentStatus.PENDING);
        repository.updateTimeslot(id, timeslot);
        return true;
    }

    /**
     * Update the status of an appointment. The current appointment status must be PENDING.
     * @param id The ID of the appointment.
     * @param status The new status of the appointment. Must be one of: ACCEPTED, REJECTED, CANCELLED
     * @return True if the appointment was successfully cancelled, false otherwise.
     */
    public boolean updateStatus(String id, AppointmentStatus status){
        if(!hasAppointment(id)) return false;

        Appointment appointment = repository.get(id);

        // Check if the appointment status is valid for updating
        if(appointment.getStatus() != AppointmentStatus.PENDING) return false;

        // Cehck if the new status is valid
        if(status != AppointmentStatus.ACCEPTED
            && status != AppointmentStatus.REJECTED
            && status != AppointmentStatus.CANCELLED){
            return false;
        }

        repository.updateStatus(id, status);
        return true;
    }
}
