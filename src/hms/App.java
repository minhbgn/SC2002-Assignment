/*
 * This source file was generated by the Gradle 'init' task
 */
package hms;

import hms.manager.AppointmentManager;
import hms.manager.InventoryManager;
import hms.manager.ManagerContext;
import hms.manager.PrescriptionManager;
import hms.manager.UserManager;
import hms.system.ISystem;
import hms.system.LoginSystem;
import hms.ui.Prompt;

/**
 * The application entry point for the Hospital Management System.
 */
public class App {
    private static final String PATIENT_REPO_FILEPATH = "data/patients.csv";
    private static final String DOCTOR_REPO_FILEPATH = "data/doctors.csv";
    private static final String PHARMACIST_REPO_FILEPATH = "data/pharmacists.csv";
    private static final String ADMIN_REPO_FILEPATH = "data/admins.csv";
    private static final String APPOINTMENT_REPO_FILEPATH = "data/appointments.csv";
    private static final String PRESCRIPTION_REPO_FILEPATH = "data/prescriptions.csv";
    private static final String INVENTORY_REPO_FILEPATH = "data/inventory.csv";

    /** Flag to check if the application has been initialized. */
    private boolean isInitialized = false;
    /** The current system that is running. */
    private ISystem currentSystem = null;

    /**
     * Initializes the Hospital Management System.
     * This method should be called before running the application.
     * <p>
     * It will:
     * - Initialize the managers and load data from the repository files.
     * - Create the login system and set it as the current system.
     * - Add a shutdown hook to save data when the application exits.
     */
    public void initialize() {
        if (isInitialized) return;

        ManagerContext ctx = new ManagerContext();

        ctx.addManager(UserManager.class,
            new UserManager(ctx,
                PATIENT_REPO_FILEPATH, 
                DOCTOR_REPO_FILEPATH, 
                PHARMACIST_REPO_FILEPATH, 
                ADMIN_REPO_FILEPATH
            )
        );

        ctx.addManager(AppointmentManager.class, new AppointmentManager(ctx, APPOINTMENT_REPO_FILEPATH));
        ctx.addManager(PrescriptionManager.class, new PrescriptionManager(ctx, PRESCRIPTION_REPO_FILEPATH));
        ctx.addManager(InventoryManager.class, new InventoryManager(ctx, INVENTORY_REPO_FILEPATH));

        currentSystem = new LoginSystem(ctx);

        // Add a shutdown hook to save data
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving data...");
            ctx.save();
        }));

        isInitialized = true;
    }

    /**
     * Runs the Hospital Management System.
     * This method should be called after initializing the application.
     * <p>
     * It will run the current system until it returns null.
     * After that, it will print a message and exit the application.
     * <p>
     * It will also pause and clear the console before continuing to the next system.
     * This is to allow the user to read the output before continuing.
     * @throws IllegalStateException if the application has not been initialized.
     */
    public void run() {
        if (!isInitialized){
            throw new IllegalStateException("HMS not initialized");
        }

        while (currentSystem != null) {
            currentSystem = currentSystem.run();
            
            // Pause before continuing
            Prompt.getStringInput("Press Enter to continue...");

            // Clear the console
            System.out.print("\033[H\033[2J");
        }

        System.out.println("Exiting HMS Application");
    }
}
