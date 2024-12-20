package hms.system.service;

import hms.manager.ManagerContext;
import hms.manager.UserManager;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.repository.AdminRepository;
import hms.user.repository.DoctorRepository;
import hms.user.repository.PatientRepository;
import hms.user.repository.PharmacistRepository;
import java.util.Date;

/**
 * Service to for adding a new user to the system.
 * <p>
 * The user selects the type of user to add and is prompted for the user's details.
 */
public class AddUserService implements IService {
    /** The manager context. */
    private final ManagerContext ctx;

    /**
     * Creates a new AddUserService.
     * @param ctx The manager context.
     */
    public AddUserService(ManagerContext ctx) {
        this.ctx = ctx;
    }

    /** Handles adding a new patient to the system. */
    private void handleAddPatient() {
        String name = Prompt.getStringInput("Enter the patient's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the patient male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the patient's contact info: ");
        Date dob = Prompt.getDateInput("Enter the patient's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .createPatient(name, isMale, contact, dob);
    }

    /** Handles adding a new doctor to the system. */
    private void handleAddDoctor() {
        String name = Prompt.getStringInput("Enter the doctor's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the doctor male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the doctor's contact info: ");
        Date dob = Prompt.getDateInput("Enter the doctor's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .createDoctor(name, isMale, contact, dob);
    }

    /** Handles adding a new pharmacist to the system. */
    private void handleAddPharmacist() {
        String name = Prompt.getStringInput("Enter the pharmacist's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the pharmacist male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the pharmacist's contact info: ");
        Date dob = Prompt.getDateInput("Enter the pharmacist's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(PharmacistRepository.class)
            .createPharmacist(name, isMale, contact, dob);
    }

    /** Handles adding a new admin to the system. */
    private void handleAddAdmin() {
        String name = Prompt.getStringInput("Enter the admin's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the admin male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the admin's contact info: ");
        Date dob = Prompt.getDateInput("Enter the admin's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(AdminRepository.class)
            .createAdmin(name, isMale, contact, dob);
    }

    /** Gets the menu for selecting the type of user to add. */
    private AbstractMenu getMenu(){
        SimpleMenu menu = new SimpleMenu("Which type of user would you like to add?", null);

        menu.addOption(new UserOption("Patient", this::handleAddPatient));
        menu.addOption(new UserOption("Doctor", this::handleAddDoctor));
        menu.addOption(new UserOption("Pharmacist", this::handleAddPharmacist));
        menu.addOption(new UserOption("Admin", this::handleAddAdmin));

        return menu;
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        menuNav.addMenu(getMenu());
    }
}
