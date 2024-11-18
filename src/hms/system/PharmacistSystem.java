package hms.system;

import hms.manager.ManagerContext;
import hms.system.service.IService;
import hms.system.service.ViewAppointmentRecordsServices;
import hms.system.service.ViewInventoryService;
import hms.system.service.ViewProfileService;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Pharmacist;
import java.util.List;

/** The pharmacist system */
public class PharmacistSystem implements ISystem {
    /** The manager context. */
    private final ManagerContext ctx;
    /** The menu navigator. */
    private final MenuNavigator menuNav;
    /**
     * The services available in this system. Contents:
     * 1. View Profile
     * 2. View Appointments Records
     * 3. View Inventory
     */
    private final IService[] services;

    /** The next system to run. */
    private ISystem nextSystem = null;

    /**
     * Create a new pharmacist system
     * @param ctx The manager context
     * @param pharmacist The pharmacist
     */
    public PharmacistSystem(ManagerContext ctx, Pharmacist pharmacist) {
        this.ctx = ctx;
        this.menuNav = new MenuNavigator();
    
        // Initialize services supported by this system
        ViewAppointmentRecordsServices viewAppointmentRecordsServices =
            new ViewAppointmentRecordsServices(ctx);
        viewAppointmentRecordsServices.hasPrescriptionUpdateOption = true;

        ViewInventoryService viewInventoryService =
            new ViewInventoryService(ctx);
        viewInventoryService.hasRequestItemOption = true;

        this.services = new IService[] {
            new ViewProfileService(pharmacist),
            viewAppointmentRecordsServices,
            viewInventoryService
        };

        // Set the next system to this system by default
        this.nextSystem = this;

        // Add the menu to the navigator
        menuNav.addMenu(getMenu());
    }

    /**
     * Get the menu for the pharmacist system
     * @return The menu
     */
    private AbstractMenu getMenu() {
        return new SimpleMenu("Welcome to the Hospital Management System!", List.of(
            new UserOption("View Profile", () -> services[0].execute(menuNav)),
            new UserOption("View Appointment Records", () -> services[1].execute(menuNav)),
            new UserOption("View Inventory", () -> services[2].execute(menuNav)),
            new UserOption("Log Out", () -> nextSystem = new LoginSystem(ctx)),
            new UserOption("Exit", () -> nextSystem = null)
        ));
    }

    /**
     * Runs the PharmacistSystem, displaying the menu and handling user input.
     * 
     * @return the next system to run
     */
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
