/*
 * This source file was generated by the Gradle 'init' task
 */
package hms;

public class App {
    private static final String PATIENT_REPO_FILEPATH = "data/patients.csv";
    private static final String DOCTOR_REPO_FILEPATH = "data/doctors.csv";
    private static final String PHARMACIST_REPO_FILEPATH = "data/pharmacists.csv";
    private static final String ADMIN_REPO_FILEPATH = "data/admins.csv";
    private static final String APPOINTMENT_REPO_FILEPATH = "data/appointments.csv";
    private static final String PRESCRIPTION_REPO_FILEPATH = "data/prescriptions.csv";
    private static final String INVENTORY_REPO_FILEPATH = "data/inventory.csv";

    private boolean isInitialized = false;
    private boolean isRunning = false;

    public void initialize() {
        if (isInitialized) return;

        // TODO: Add initialization code here:
        // 1. Initialize manager context
        // 2. Add managers to context
        // 3. ...

        isInitialized = true;
    }

    public void run() {
        if (!isInitialized){
            throw new IllegalStateException("HMS not initialized");
        }

        if (isRunning) return;

        isRunning = true;
        while (isRunning) {
            // TODO: Add main loop code here
            // 1. Display login screen
            // 2. Parse user input, validate and authenticate user
            // 3. Redirect to appropriate user interface depending on user role
            // 4. ...

            break; // Temporary break to avoid infinite loop, remove this line after implementing the exit condition
        }
    }
}
