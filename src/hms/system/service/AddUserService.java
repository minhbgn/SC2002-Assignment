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
 * This class is used by the Administrators to add new users into the system.
 */
public class AddUserService implements IService {
    private final ManagerContext ctx;

    /**
     * Constructor for AddUserService class
     * @param ctx The manager context to manage other classes. In this case, the User
     */
    public AddUserService(ManagerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Adding a patient into the system. 
     * Inputs required from a user are name, gender, contact and day of birth
     */
    private void handleAddPatient() {
        String name = Prompt.getStringInput("Enter the patient's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the patient male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the patient's contact info: ");
        Date dob = Prompt.getDateInput("Enter the patient's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .createPatient(name, isMale, contact, dob);
    }

    /**
     * Adding a doctor into the system. 
     * Inputs required from a user are name, gender, contact and day of birth
     */  
    private void handleAddDoctor() {
        String name = Prompt.getStringInput("Enter the doctor's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the doctor male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the doctor's contact info: ");
        Date dob = Prompt.getDateInput("Enter the doctor's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .createDoctor(name, isMale, contact, dob);
    }
    
    /**
     * Adding a pharmacist into the system. 
     * Inputs required from a user are name, gender, contact and day of birth
     */
    private void handleAddPharmacist() {
        String name = Prompt.getStringInput("Enter the pharmacist's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the pharmacist male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the pharmacist's contact info: ");
        Date dob = Prompt.getDateInput("Enter the pharmacist's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(PharmacistRepository.class)
            .createPharmacist(name, isMale, contact, dob);
    }

    /**
     * Adding an admin into the system. 
     * Inputs required from a user are name, gender, contact and day of birth
     */
    private void handleAddAdmin() {
        String name = Prompt.getStringInput("Enter the admin's name: ");
        boolean isMale = Prompt.getBooleanInput("Is the admin male? (Y/N) ");
        String contact = Prompt.getStringInput("Enter the admin's contact info: ");
        Date dob = Prompt.getDateInput("Enter the admin's date of birth: ");

        ctx.getManager(UserManager.class)
            .getRepository(AdminRepository.class)
            .createAdmin(name, isMale, contact, dob);
    }

    /**
     * Get the menu consist of different kinds of user to be added in
     * @return the menu
     */
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
