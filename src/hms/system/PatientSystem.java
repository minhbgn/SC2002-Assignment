package hms.system;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.system.service.IService;
import hms.system.service.ScheduleAppointmentService;
import hms.system.service.ViewAppointmentsService;
import hms.system.service.ViewProfileService;
import hms.system.service.ViewRecordsService;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Patient;
import java.util.List;

/** The patient system */
public class PatientSystem implements ISystem {
    /** The manager context. */
    private final ManagerContext ctx;
    /** The menu navigator. */
    private final MenuNavigator menuNav;
    /**
     * The services available in this system. Contents:
     * 1. View Profile
     * 2. View Medical Records
     * 3. View Appointments
     * 4. Schedule Appointment
     */
    private final IService[] services;

    /** The next system to run. */
    private ISystem nextSystem = null;

    /**
     * Create a new patient system
     * @param ctx The manager context
     * @param patient The patient
     */
    public PatientSystem(ManagerContext ctx, Patient patient) {
        this.ctx = ctx;
        this.menuNav = new MenuNavigator();
    
        // Default search criteria for appointments
        List<SearchCriterion<Appointment, ?>> defaultCriteria = List.of(
            new SearchCriterion<>(Appointment::getPatientId, patient.getAccount().getId())
        );

        ViewAppointmentsService viewAppointmentsService = new ViewAppointmentsService(ctx, defaultCriteria);
        viewAppointmentsService.hasRescheduleAppointmentOption = true;
        viewAppointmentsService.hasCancelAppointmentOption = true;
        viewAppointmentsService.hasViewRecordsOption = true;

        // Initialize services supported by this system
        this.services = new IService[] {
            new ViewProfileService(patient),
            new ViewRecordsService(patient),
            viewAppointmentsService,
            new ScheduleAppointmentService(ctx, patient),
        };

        // Set the next system to this system by default
        this.nextSystem = this;

        // Add the menu to the navigator
        menuNav.addMenu(getMenu());
    }

    /**
     * Get the menu for the patient system
     * @return The menu
     */
    private AbstractMenu getMenu() {
        return new SimpleMenu("Welcome to the Hospital Management System!", List.of(
            new UserOption("View Profile", () -> services[0].execute(menuNav)),
            new UserOption("View Medical Records", () -> services[1].execute(menuNav)),
            new UserOption("View Appointments", () -> services[2].execute(menuNav)),
            new UserOption("Schedule Appointment", () -> services[3].execute(menuNav)),
            new UserOption("Log Out", () -> nextSystem = new LoginSystem(ctx)),
            new UserOption("Exit", () -> nextSystem = null)
        ));
    }

    @Override
    public ISystem run() {
        menuNav.display(true);

        String choice = Prompt.getStringInput("Enter your choice: ");

        if (!menuNav.hasOption(choice)){
            System.out.println("Invalid choice");
            return this;
        }

        menuNav.executeOption(choice);

        return nextSystem;
    }
}
