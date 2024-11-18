package hms.system;

import hms.common.IModel;
import hms.manager.ManagerContext;
import hms.manager.UserManager;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Admin;
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.model.Pharmacist;
import hms.user.repository.AdminRepository;
import hms.user.repository.DoctorRepository;
import hms.user.repository.PatientRepository;
import hms.user.repository.PharmacistRepository;
import java.util.List;

/**
 * The login system for the Hospital Management System.
 * This system allows users to login to the system and access their respective systems.
 * Users can login as an Admin, Doctor, Patient, or Pharmacist.
 * The next system to run is determined based on the user type.
 */
public class LoginSystem implements ISystem {
    /** Manager context */
    private final ManagerContext ctx;
    /** The next system to run. This is set by the run method and returned to the caller. */
    private ISystem nextSystem = null;
    /** The login menu */
    private final SimpleMenu menu;

    /**
     * Create a new login system
     * @param ctx The manager context
     */
    public LoginSystem(ManagerContext ctx){
        this.ctx = ctx;
        this.menu = new SimpleMenu("Welcome to the Hospital Management System", List.of(
            new UserOption("Login", this::login),
            new UserOption("Exit", () -> nextSystem = null)
        ));
    }

    /**
     * Display the login menu and process user input. Users can login or exit the system.
     * @return The next system to run.
     * - If login is successful, the next system is determined based on the user type.
     * - If login fails, the next system is the current system.
     * - If the user chooses to exit, the next system is null.
     */
    @Override
    public ISystem run() {
        menu.display(true);
        
        String choice = Prompt.getStringInput("Enter your choice: ");

        if(menu.hasOption(choice)){
            menu.executeOption(choice);
        }
        else{
            System.out.println("Invalid choice");
            nextSystem = this;
        }

        return nextSystem;
    }

    /** Display the login prompt and authenticate the user, updating the next system accordingly. */
    private void login() {
        String userId = Prompt.getStringInput("Enter your user ID: ");
        String password = Prompt.getStringInput("Enter your password: ");

        Class<? extends IModel> userClass = ctx
            .getManager(UserManager.class)
            .authenticate(userId, password);

        if(userClass == null){
            System.out.println("Invalid user ID or password");
            nextSystem = this;
        }
        else{
            switch (userClass.getSimpleName()) {
                case "Admin" -> {
                    Admin admin = ctx
                        .getManager(UserManager.class)
                        .getRepository(AdminRepository.class)
                        .get(userId);
                    nextSystem = new AdminSystem(ctx, admin);
                }
                case "Doctor" -> {
                    Doctor doctor = ctx
                        .getManager(UserManager.class)
                        .getRepository(DoctorRepository.class)
                        .get(userId);
                    nextSystem = new DoctorSystem(ctx, doctor);
                }
                case "Patient" -> {
                    Patient patient = (Patient) ctx
                        .getManager(UserManager.class)
                        .getRepository(PatientRepository.class)
                        .get(userId);
                    nextSystem = new PatientSystem(ctx, patient);
                }
                case "Pharmacist" -> {
                    Pharmacist pharmacist = ctx.getManager(UserManager.class)
                        .getRepository(PharmacistRepository.class)
                        .get(userId);
                    nextSystem = new PharmacistSystem(ctx, pharmacist);
                }
                default -> throw new IllegalStateException("Unexpected value: " + userClass.getSimpleName());
            }
        }
    }

}
