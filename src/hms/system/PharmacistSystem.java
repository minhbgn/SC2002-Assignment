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

public class PharmacistSystem implements ISystem {
    /** The menu navigator for this system. */
    private final MenuNavigator menuNav;
    /** The next system to run. */
    private ISystem nextSystem = null;
    /**
     * The services available in this system. Contents:
     * 1. View Profile
     * 2. View Appointments Records
     * 3. View Inventory
     */
    private final IService[] services;

    public PharmacistSystem(ManagerContext ctx, Pharmacist pharmacist) {
        this.menuNav = new MenuNavigator();
    
        // Initialize services supported by this system
        this.services = new IService[] {
            new ViewProfileService(pharmacist),
            new ViewAppointmentRecordsServices(ctx),
            new ViewInventoryService(ctx)
        };

        // Set the next system to this system by default
        this.nextSystem = this;

        // Add the menu to the navigator
        menuNav.addMenu(getMenu());
    }

    private AbstractMenu getMenu() {
        return new SimpleMenu("Welcome to the Hospital Management System!", List.of(
            new UserOption("View Profile", () -> services[0].execute(menuNav)),
            new UserOption("View Appointment Records", () -> services[1].execute(menuNav)),
            new UserOption("View Inventory", () -> services[2].execute(menuNav)),
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