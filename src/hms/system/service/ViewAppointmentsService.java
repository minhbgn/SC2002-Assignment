package hms.system.service;

import hms.appointment.Appointment;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import java.util.ArrayList;
import java.util.List;

public class ViewAppointmentsService implements IService {
    private final ManagerContext ctx;
    private final List<SearchCriterion<Appointment, ?>> defaultCriteria;
    private final List<SearchCriterion<Appointment, ?>> activeCriteria = new ArrayList<>();

    public boolean hasCancelAppointmentOption = false;
    public boolean hasCompleteAppointmentOption = false;
    public boolean hasRescheduleAppointmentOption = false;
    public boolean hasResolveAppointmentOption = false;
    public boolean hasUpdatePatientMedicalRecordOption = false;
    public boolean hasViewPatientInfoOption = false;
    public boolean hasViewPatientMedicalRecordOption = false;
    public boolean hasViewRecordsOption = false;

    /** Bound menu navigator */
    private MenuNavigator menuNav;

    /**
     * Constructor for ViewAppointmentsService.
     * 
     * @param ctx the ManagerContext
     * @param defaultCriteria the default search criteria
     */
    public ViewAppointmentsService(ManagerContext ctx,
        List<SearchCriterion<Appointment, ?>> defaultCriteria) {

        this.ctx = ctx;
        this.defaultCriteria = defaultCriteria;
    }

    /**
     * Handles the filter menu for appointments.
     */
    private void handleFilter(){
        SimpleMenu filterChoicesMenu = new SimpleMenu("Filter by:", List.of(
            new UserOption("Pending", () -> setFilter(AppointmentStatus.PENDING)),
            new UserOption("Cancelled", () -> setFilter(AppointmentStatus.CANCELLED)),
            new UserOption("Accepted", () -> setFilter(AppointmentStatus.ACCEPTED)),
            new UserOption("Rejected", () -> setFilter(AppointmentStatus.REJECTED)),
            new UserOption("Finished", () -> setFilter(AppointmentStatus.FINISHED)),
            new UserOption("All", () -> setFilter(null))
        ));

        menuNav.addMenu(filterChoicesMenu);
    }

    /**
     * Sets the filter for appointments based on the given status.
     * 
     * @param status the AppointmentStatus to filter by
     */
    private void setFilter(AppointmentStatus status){
        activeCriteria.clear();

        if(status != null){
            activeCriteria.add(new SearchCriterion<>(Appointment::getStatus, status));
        }

        // Update the entire menu display
        menuNav.popMenu(); // Pop the filter menu
        menuNav.popMenu(); // Pop the appointment list menu
        menuNav.addMenu(getMenu()); // Re-add the appointment list menu with the new filter
    }

    /**
     * Handles the selection of an appointment.
     * 
     * @param appointment the selected Appointment
     */
    private void onAppointmentSelect(Appointment appointment){
        ViewAppointmentDetailsService service = new ViewAppointmentDetailsService(ctx, appointment);

        // Assign the options to the service
        service.hasCancelAppointmentOption = hasCancelAppointmentOption;
        service.hasCompleteAppointmentOption = hasCompleteAppointmentOption;
        service.hasRescheduleAppointmentOption = hasRescheduleAppointmentOption;
        service.hasResolveAppointmentOption = hasResolveAppointmentOption;
        service.hasUpdatePatientMedicalRecordOption = hasUpdatePatientMedicalRecordOption;
        service.hasViewPatientInfoOption = hasViewPatientInfoOption;
        service.hasViewPatientMedicalRecordOption = hasViewPatientMedicalRecordOption;
        service.hasViewRecordsOption = hasViewRecordsOption;

        service.execute(menuNav);
    }

    /**
     * Gets the menu for displaying appointments.
     * 
     * @return the AbstractMenu for appointments
     */
    private AbstractMenu getMenu() {
        List<SearchCriterion<Appointment, ?>> criteria = new ArrayList<>();
        if(defaultCriteria != null) criteria.addAll(defaultCriteria);
        if(activeCriteria != null) criteria.addAll(activeCriteria);

        List<Appointment> appointments = ctx.getManager(AppointmentManager.class)
            .getAppointments(criteria);

        AbstractMenu returnMenu;

        if (appointments.isEmpty()) returnMenu = new SimpleMenu("No appointments found", null);
        else returnMenu = new PaginatedListSelector<>(
            "Appointments", appointments.toArray(Appointment[]::new),
            this::onAppointmentSelect
        );

        returnMenu.addOption("f", new UserOption("Filter", this::handleFilter));

        return returnMenu;
    }
    
    /**
     * Executes the service and displays the menu.
     * 
     * @param menuNav the MenuNavigator to use
     */
    @Override
    public void execute(MenuNavigator menuNav) {
        // This method is called when the service is executed
        // Hence, we need to reset the active criteria
        activeCriteria.clear();

        this.menuNav = menuNav;
        
        menuNav.addMenu(getMenu());
    }
}
