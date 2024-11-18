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

/** Service for scheduling an appointment with a doctor. */
public class ScheduleAppointmentService implements IService {
    /** The user using this service */
    private final User user;
    /** The manager context */
    private final ManagerContext ctx;

    /** Bound menu navigator */
    private MenuNavigator menuNav;

    /** The doctor selected for the appointment */
    private String selectedDoctorId;

    /**
     * Create a new ScheduleAppointmentService
     * @param ctx The manager context
     * @param user The user using this service
     */
    public ScheduleAppointmentService(ManagerContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
    }

    /**
     * Handle what happens after the user selects a doctor.
     * <p>
     * This method will pop the doctor selector menu, display the selected doctor,
     * and prompt the user to select a timeslot through the QueryFreeTimeslotService.
     * @param doctor The doctor selected
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

    /**
     * Handle what happens after the user selects a timeslot.
     * <p>
     * This method will create an appointment with the selected doctor and timeslot.
     * @param timeslot The timeslot selected
     */
    private void onTimeslotSelected(Timeslot timeslot){
        Appointment appointment = ctx
            .getManager(AppointmentManager.class)
            .makeAppointment(user.getAccount().getId(), selectedDoctorId, timeslot);

        if(appointment == null) // This should never happen
            throw new RuntimeException("Failed to schedule appointment");

        System.out.println("Appointment scheduled: " + appointment);
    }

    /**
     * Get the menu for this service.
     * <p>
     * This menu will display a list of doctors for the user to select from.
     * @return The menu
     */
    public AbstractMenu getMenu(){
        Doctor[] doctors = ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .findWithFilters(null)
            .toArray(Doctor[]::new);

        PaginatedListSelector<Doctor> doctorSelector = new PaginatedListSelector<>(
            "Select a doctor for your appointment", doctors, this::onDoctorSelected
        );

        return doctorSelector;
    }
        
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }
    
}
