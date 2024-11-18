package hms.system.service;

import hms.manager.ManagerContext;
import hms.manager.UserManager;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Admin;
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.model.Pharmacist;
import hms.user.model.User;
import java.util.List;
import java.util.function.Function;

/** Service to view all users in the system. */
public class ViewUsersService implements IService {
    /** The admin viewing the users. */
    private final Admin admin;
    /** The manager context. */
    private final ManagerContext ctx;
    
    // The options for the menu
    /** The option to disable an account. */
    private final UserOption disableAccountOption = new UserOption("Disable this account", this::handleDisableAccount);
    /** The option to enable an account. */
    private final UserOption enableAccountOption = new UserOption("Enable this account", this::handleEnableAccount);
    /** The options for account activation. */
    private final List<UserOption> accountActiveOptions = List.of(disableAccountOption, enableAccountOption);
    
    /** The menu navigator. */
    private MenuNavigator menuNav;
    /** The user currently selected. */
    private User selected;

    /**
     * Creates a new ViewUsersService.
     * @param ctx The manager context.
     * @param admin The admin viewing the users.
     */
    public ViewUsersService(ManagerContext ctx, Admin admin) {
        this.ctx = ctx;
        this.admin = admin;
    }

    /** Handles the disabling of an account. */
    private void handleDisableAccount() {
        selected.getAccount().setActive(false);
    }

    /** Handles the enabling of an account. */
    private void handleEnableAccount() {
        selected.getAccount().setActive(true);
    }

    /** Handles the selection of a user. */
    private void handleUserSelect(User user) {
        selected = user;

        AbstractMenu menu = new SimpleMenu(user.toString(), accountActiveOptions);

        menuNav.addMenu(menu);
    }

    /**
     * Handles the viewing of patients.
     */
    private void handleViewPatients() { handleUserSelect(Patient.class, size -> new Patient[size]); }

    /**
     * Handles the viewing of doctors.
     */
    private void handleViewDoctors() { handleUserSelect(Doctor.class, size -> new Doctor[size]); }

    /**
     * Handles the viewing of pharmacists.
     */
    private void handleViewPharmacists() { handleUserSelect(Pharmacist.class, size -> new Pharmacist[size]); }

    /**
     * Handles the viewing of admins.
     */
    private void handleViewAdmins() { handleUserSelect(Admin.class, size -> new Admin[size]); }

    /**
     * Handles the selection of a user type.
     * @param <T> The type of user
     * @param clazz The class of the user
     * @param arraySupplier A function that creates an array of the correct type given the size,
     * this is necessary because Java does not allow creating arrays of generic types
     */
    private <T extends User> void handleUserSelect(Class<T> clazz, Function<Integer, T[]> arraySupplier) {
        List<T> users = ctx.getManager(UserManager.class)
            .getUser(clazz, null);

        users.removeIf(p -> p.getAccount().getId().equals(admin.getAccount().getId()));

        T[] usersArray = arraySupplier.apply(users.size());
        users.toArray(usersArray);

        PaginatedListSelector<T> selector = new PaginatedListSelector<>(
            "Select a " + clazz.getName() + " to view", usersArray, this::handleUserSelect);

        menuNav.addMenu(selector);
    }

    /**
     * Returns the menu for selecting the user type.
     * @return The menu for selecting the user type.
     */
    private AbstractMenu getMenu() {
        SimpleMenu menu = new SimpleMenu("Which user type would you like to view?", null);

        menu.addOption(new UserOption("Patients", this::handleViewPatients));
        menu.addOption(new UserOption("Doctors", this::handleViewDoctors));
        menu.addOption(new UserOption("Pharmacists", this::handleViewPharmacists));
        menu.addOption(new UserOption("Admins", this::handleViewAdmins));
        
        return menu;
    }

    /**
     * Executes the service by adding the main menu to the menu navigator.
     * @param menuNav The menu navigator.
     */
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }
}
