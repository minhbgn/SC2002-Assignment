package hms.system;

import hms.common.IModel;
import hms.manager.ManagerContext;
import hms.manager.UserManager;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Patient;
import hms.user.repository.PatientRepository;
import java.util.List;

public class LoginSystem implements ISystem {
    /** Manager context */
    private final ManagerContext ctx;
    /**
     * The next system to run. This is set by the run method and returned to the caller.</p>
     */
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
     * Display the login menu and process user input. Users can login or exit the system. </p>
     * @return The next system to run. </p>
     * - If login is successful, the next system is determined based on the user type. </p>
     * - If login fails, the next system is the current system. </p>
     * - If the user chooses to exit, the next system is null. </p>
     */
    @Override
    public ISystem run() {
        menu.display();
        
        Prompt prompt = new Prompt("Enter your choice: ");
        String choice = prompt.getStringInput();

        if(menu.hasOption(choice)){
            menu.executeOption(choice);
        }
        else{
            System.out.println("Invalid choice");
            nextSystem = this;
        }

        return nextSystem;
    }

    /**
     * Display the login prompt and authenticate the user, updating the next system accordingly. </p>
     */
    private void login() {
        Prompt userIdPrompt = new Prompt("Enter your user ID: ");
        userIdPrompt.display();
        String userId = userIdPrompt.getStringInput();

        Prompt passwordPrompt = new Prompt("Enter your password: ");
        passwordPrompt.display();
        String password = passwordPrompt.getStringInput();

        Class<? extends IModel> userClass = ctx
            .getManager(UserManager.class)
            .authenticate(userId, password);

        if(userClass == null){
            System.out.println("Invalid user ID or password");
            nextSystem = this;
        }
        else{
            switch (userClass.getSimpleName()) {
                case "Admin" -> nextSystem = new AdminSystem();
                case "Doctor" -> nextSystem = new DoctorSystem();
                case "Patient" -> {
                    Patient patient = (Patient) ctx
                        .getManager(UserManager.class)
                        .getRepository(PatientRepository.class)
                        .get(userId);
                    nextSystem = new PatientSystem(ctx, patient);
                }
                case "Pharmacist" -> nextSystem = new PharmacistSystem();
                default -> throw new IllegalStateException("Unexpected value: " + userClass.getSimpleName());
            }
        }
    }

}
