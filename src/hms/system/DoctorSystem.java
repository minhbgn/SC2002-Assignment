package hms.system;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.system.service.IService;
import hms.system.service.ViewAppointmentsService;
import hms.system.service.ViewProfileService;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Doctor;
import java.util.List;

public class DoctorSystem implements ISystem {
    /**
     * The services available in this system. Contents:
     * 1. View Profile
     * 2. View Appointments
     */
    private final IService[] services;
    private final ManagerContext ctx;
    private final MenuNavigator menuNav;

    /** The next system to run. */
    private ISystem nextSystem = null;

    public DoctorSystem(ManagerContext ctx, Doctor doctor) {
        this.ctx = ctx;
        this.menuNav = new MenuNavigator();
    
        // Default search criteria for appointments
        List<SearchCriterion<Appointment,?>> defaultCriteria = List.of(
            new SearchCriterion<>(Appointment::getDoctorId, doctor.getAccount().getId())
        );

        ViewAppointmentsService viewAppointmentsService = new ViewAppointmentsService(ctx, doctor, defaultCriteria);
        viewAppointmentsService.hasCompleteAppointmentOption = true;
        viewAppointmentsService.hasResolveAppointmentOption = true;
        viewAppointmentsService.hasViewPatientInfoOption = true;
        viewAppointmentsService.hasViewRecordsOption = true;

        // Initialize services supported by this system
        this.services = new IService[] {
            new ViewProfileService(doctor),
            viewAppointmentsService
        };

        // Set the next system to this system by default
        this.nextSystem = this;

        // Add the menu to the navigator
        menuNav.addMenu(getMenu());
    }

    private AbstractMenu getMenu() {
        return new SimpleMenu("Welcome to the Hospital Management System!", List.of(
            new UserOption("View Profile", () -> services[0].execute(menuNav)),
            new UserOption("View Appointments", () -> services[1].execute(menuNav)),
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
