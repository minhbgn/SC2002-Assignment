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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LoginSystem implements ISystem {
    /** Mapping of user model class to system supplier */
    private static final Map<Class<? extends IModel>, Supplier<ISystem>> USER_SYSTEM_MAPPING = Map.of(
        Admin.class, AdminSystem::new,
        Doctor.class, DoctorSystem::new,
        Patient.class, PatientSystem::new,
        Pharmacist.class, PharmacistSystem::new
    );
    /** Manager context */
    private final ManagerContext ctx;
    /**
     * The next system to run. This is set by the run method and returned to the caller.</p>
     */
    private ISystem nextSystem = null;

    /**
     * Create a new login system
     * @param ctx The manager context
     */
    public LoginSystem(ManagerContext ctx){
        this.ctx = ctx;
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
        SimpleMenu menu = new SimpleMenu("Welcome to the Hospital Management System", List.of(
            new UserOption("Login", this::login),
            new UserOption("Exit", () -> nextSystem = null)
        ));

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
            nextSystem = USER_SYSTEM_MAPPING.get(userClass).get();
        }
    }

}
