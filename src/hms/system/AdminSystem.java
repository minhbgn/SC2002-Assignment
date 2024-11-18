package hms.system;

import hms.manager.ManagerContext;
import hms.system.service.AddUserService;
import hms.system.service.IService;
import hms.system.service.ViewAppointmentRecordsServices;
import hms.system.service.ViewAppointmentsService;
import hms.system.service.ViewInventoryService;
import hms.system.service.ViewProfileService;
import hms.system.service.ViewUsersService;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Admin;
import java.util.List;

/** The system for an admin user. */
public class AdminSystem implements ISystem {
    /** The manager context. */
    private final ManagerContext ctx;
    /** The menu navigator. */
    private final MenuNavigator menuNav;
    /**
     * The services available in this system. Contents:
     * 1. View Profile
     * 2. View Appointments
     * 3. View Appointment Records
     * 4. View Inventory
     * 5. View Users
     * 6. Add User
     */
    private final IService[] services;

    /** The next system to run. */
    private ISystem nextSystem = null;

    /**
     * Creates a new AdminSystem.
     * @param ctx The manager context.
     * @param admin The admin user.
     */
    public AdminSystem(ManagerContext ctx, Admin admin) {
        this.ctx = ctx;
        this.menuNav = new MenuNavigator();
    
        // Initialize services supported by this system
        ViewInventoryService viewInventoryService = new ViewInventoryService(ctx);
        viewInventoryService.hasChangeLowStockAlertOption = true;
        viewInventoryService.hasResolveRequestOption = true;
        viewInventoryService.hasUpdateStockOption = true;

        ViewAppointmentsService viewAppointmentsService = new ViewAppointmentsService(ctx, null);
        viewAppointmentsService.hasViewRecordsOption = true;

        this.services = new IService[] {
            new ViewProfileService(admin),
            viewAppointmentsService,
            new ViewAppointmentRecordsServices(ctx),
            viewInventoryService,
            new ViewUsersService(ctx, admin),
            new AddUserService(ctx)
        };

        // Set the next system to this system by default
        this.nextSystem = this;

        // Add the menu to the navigator
        menuNav.addMenu(getMenu());
    }

    /**
     * Returns a menu that displays the options available to the admin.
     * @return A menu that displays the options available to the admin.
     */
    private AbstractMenu getMenu() {
        return new SimpleMenu("Welcome to the Hospital Management System!", List.of(
            new UserOption("View Profile", () -> services[0].execute(menuNav)),
            new UserOption("View Appointments", () -> services[1].execute(menuNav)),
            new UserOption("View Appointment Records", () -> services[2].execute(menuNav)),
            new UserOption("View Inventory", () -> services[3].execute(menuNav)),
            new UserOption("View Users", () -> services[4].execute(menuNav)),
            new UserOption("Add User", () -> services[5].execute(menuNav)),
            new UserOption("Log Out", () -> nextSystem = new LoginSystem(ctx)),
            new UserOption("Exit", () -> nextSystem = null)
        ));
    }

    @Override
    public ISystem run() {
        menuNav.display(true);

        String choice = Prompt.getStringInput("Enter your choice: ");

        if (!menuNav.hasOption(choice)){
            System.out.println("Invalid choice");
            return this;
        }

        menuNav.executeOption(choice);

        return nextSystem;
    }
}
