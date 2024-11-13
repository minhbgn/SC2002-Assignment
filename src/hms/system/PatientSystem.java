package hms.system;

import hms.appointment.Appointment;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListViewer;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Patient;
import java.util.Date;
import java.util.List;

public class PatientSystem implements ISystem {
    /** The patient bound to this system */
    private final Patient patient;
    /** The active search criteria for appointments */
    private final List<SearchCriterion<Appointment, ?>> defaultCriteria;

    private final ManagerContext ctx;
    private final MenuNavigator menuNav;

    /** The next system to run. */
    private ISystem nextSystem = null;

    /**
     * Create a new patient system
     * @param ctx The manager context
     * @param patient The patient
     */
    public PatientSystem(ManagerContext ctx, Patient patient) {
        this.patient = patient;
        this.ctx = ctx;
        this.menuNav = new MenuNavigator("Patient Menu");
    
        // Default search criteria for appointments
        this.defaultCriteria = List.of(
            new SearchCriterion<>(Appointment::getPatientId, patient.getAccount().getId())
        );

        this.nextSystem = this; // Default to the current system

        menuNav.addMenu(new SimpleMenu("Welcome to the Hospital Management System!", List.of(
            new UserOption("View Profile", this::handleViewProfile),
            new UserOption("View Appointments", this::handleViewAppointments),
            new UserOption("Schedule Appointment", this::handleScheduleAppointment),
            new UserOption("Log Out", () -> nextSystem = new LoginSystem(ctx)),
            new UserOption("Exit", () -> nextSystem = null)
        )));
    }
    
    private void handleViewProfile() {
        System.out.println(patient);
    }

    private void handleViewAppointments() {
        List<Appointment> appointments = ctx
            .getManager(AppointmentManager.class)
            .getAppointments(defaultCriteria);

        if (appointments.isEmpty()) {
            System.out.println("You have not made any appointments");
            return;
        }
        
        PaginatedListViewer<Appointment> viewer = new PaginatedListViewer<>(
            "Appointments",
            appointments.toArray(Appointment[]::new)
        );
        
        menuNav.addMenu(viewer);
    }

    private void handleScheduleAppointment() {
        System.out.println("Scheduling an appointment");

        String doctorId = Prompt.getStringInput("Enter the doctor's ID: ");
        Date date = Prompt.getDateInput("Enter the date: ");

        Appointment appointment = ctx
            .getManager(AppointmentManager.class)
            .makeAppointment(patient.getAccount().getId(), doctorId, date);

        System.out.println("Appointment scheduled: " + appointment);
    }

    @Override
    public ISystem run() {
        menuNav.display();

        String choice = Prompt.getStringInput("Enter your choice: ");

        if (!menuNav.hasOption(choice)){
            System.out.println("Invalid choice");
            return this;
        }

        menuNav.executeOption(choice);

        return nextSystem;
    }
}
