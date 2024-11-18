package hms.system.service;

import hms.appointment.Appointment;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.manager.UserManager;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.Prompt;
import hms.user.model.Doctor;
import hms.user.model.User;
import hms.user.repository.DoctorRepository;
import hms.utils.Timeslot;

public class ScheduleAppointmentService implements IService {
    /** The user using this service */
    private final User user;
    private final ManagerContext ctx;

    /**
     * Bound menu navigator
     */
    private MenuNavigator menuNav;

    /**
     * The doctor selected for the appointment
     */
    private String selectedDoctorId;

    /**
     * Constructs a ScheduleAppointmentService with the given context and user.
     *
     * @param ctx the manager context
     * @param user the user
     */
    public ScheduleAppointmentService(ManagerContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
    }

    /**
     * Handles the event when a doctor is selected.
     *
     * @param doctor the selected doctor
     */
    private void onDoctorSelected(Doctor doctor){
        menuNav.popMenu(); // Pop the doctor selector

        selectedDoctorId = doctor.getAccount().getId();

        // Clear the screen
        System.out.print("\033[H\033[2J");

        System.out.println("Selected doctor: " + doctor);

        boolean confirm = Prompt.getBooleanInput("Would you like to " + 
            "schedule an appointment with this doctor? (y/n): ");
        if(!confirm) return;
        
        QueryFreeTimeslotService queryFreeTimeslotService = new QueryFreeTimeslotService(ctx, this::onTimeslotSelected);

        queryFreeTimeslotService.bindDoctor(selectedDoctorId);
        queryFreeTimeslotService.bindUser(user.getAccount().getId());

        queryFreeTimeslotService.execute(menuNav);
    }

    private void onTimeslotSelected(Timeslot timeslot){
        Appointment appointment = ctx
            .getManager(AppointmentManager.class)
            .makeAppointment(user.getAccount().getId(), selectedDoctorId, timeslot);

        if(appointment == null) // This should never happen
            throw new RuntimeException("Failed to schedule appointment");

        System.out.println("Appointment scheduled: " + appointment);
    }

    /**
     * Schedules an appointment.
     * 
     * @param appointment the appointment to be scheduled
     * @throws RuntimeException if the appointment is null
     */
    public void scheduleAppointment(Appointment appointment) {
        if(appointment == null) // This should never happen
            throw new RuntimeException("Failed to schedule appointment");

        System.out.println("Appointment scheduled: " + appointment);
    }

    /**
     * Retrieves the menu for selecting a doctor.
     * 
     * @return the menu for selecting a doctor
     */
    public AbstractMenu getMenu() {
        Doctor[] doctors = ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .findWithFilters(null)
            .toArray(Doctor[]::new);

        PaginatedListSelector<Doctor> doctorSelector = new PaginatedListSelector<>(
            "Select a doctor for your appointment", doctors, this::onDoctorSelected
        );

        return doctorSelector;
    }

    /**
     * Executes the menu navigation.
     * 
     * @param menuNav the menu navigator
     */
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }
    
}
