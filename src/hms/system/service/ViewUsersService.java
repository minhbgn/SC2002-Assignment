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

public class ViewUsersService implements IService {
    private final Admin admin;
    private final ManagerContext ctx;
    
    private final UserOption disableAccountOption = new UserOption("Disable this account", this::handleDisableAccount);
    private final UserOption enableAccountOption = new UserOption("Enable this account", this::handleEnableAccount);
    private final List<UserOption> accountActiveOptions = List.of(disableAccountOption, enableAccountOption);
    
    private MenuNavigator menuNav;
    
    private User selected;

    public ViewUsersService(ManagerContext ctx, Admin admin) {
        this.ctx = ctx;
        this.admin = admin;
    }

    private void handleDisableAccount() {
        selected.getAccount().setActive(false);
    }

    private void handleEnableAccount() {
        selected.getAccount().setActive(true);
    }

    private void handleUserSelect(User user) {
        selected = user;

        AbstractMenu menu = new SimpleMenu(user.toString(), accountActiveOptions);

        menuNav.addMenu(menu);
    }

    private void handleViewPatients() { handleUserSelect(Patient.class, size -> new Patient[size]); }
    private void handleViewDoctors() { handleUserSelect(Doctor.class, size -> new Doctor[size]); }
    private void handleViewPharmacists() { handleUserSelect(Pharmacist.class, size -> new Pharmacist[size]); }
    private void handleViewAdmins() { handleUserSelect(Admin.class, size -> new Admin[size]); }

    // arraySupplier is necessary to create an array of the correct type
    // because Java does not allow creating arrays of generic types :(

    /**
     * Handles the selection of a user type.
     * @param <T> The type of user
     * @param clazz The class of the user
     * @param arraySupplier A function that creates an array of the correct type
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

    private AbstractMenu getMenu() {
        SimpleMenu menu = new SimpleMenu("Which user type would you like to view?", null);

        menu.addOption(new UserOption("Patients", this::handleViewPatients));
        menu.addOption(new UserOption("Doctors", this::handleViewDoctors));
        menu.addOption(new UserOption("Pharmacists", this::handleViewPharmacists));
        menu.addOption(new UserOption("Admins", this::handleViewAdmins));
        
        return menu;
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }
}
