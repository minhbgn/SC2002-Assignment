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

/** Service to view appointments */
public class ViewAppointmentsService implements IService {
    /** Manager context */
    private final ManagerContext ctx;
    /** Default search criteria */
    private final List<SearchCriterion<Appointment, ?>> defaultCriteria;
    /** Active search criteria */
    private final List<SearchCriterion<Appointment, ?>> activeCriteria = new ArrayList<>();

    // Options
    /** Whether the service has the option to cancel an appointment */
    public boolean hasCancelAppointmentOption = false;
    /** Whether the service has the option to complete an appointment */
    public boolean hasCompleteAppointmentOption = false;
    /** Whether the service has the option to reschedule an appointment */
    public boolean hasRescheduleAppointmentOption = false;
    /** Whether the service has the option to resolve an appointment */
    public boolean hasResolveAppointmentOption = false;
    /** Whether the service has the option to update a patient's medical record */
    public boolean hasUpdatePatientMedicalRecordOption = false;
    /** Whether the service has the option to view a patient's information */
    public boolean hasViewPatientInfoOption = false;
    /** Whether the service has the option to view a patient's medical record */
    public boolean hasViewPatientMedicalRecordOption = false;
    /** Whether the service has the option to view records */
    public boolean hasViewRecordsOption = false;

    /** Bound menu navigator */
    private MenuNavigator menuNav;

    /**
     * Constructor
     * @param ctx Manager context
     * @param defaultCriteria Default search criteria to use when fetching appointments
     */
    public ViewAppointmentsService(ManagerContext ctx,
        List<SearchCriterion<Appointment, ?>> defaultCriteria) {

        this.ctx = ctx;
        this.defaultCriteria = defaultCriteria;
    }

    /** Handler for the filter option. Only filters by apponitment status */
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
     * Set the filter for the appointments based on the status
     * @param status Status to filter by
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
     * Handler for when an appointment is selected
     * @param appointment Appointment that was selected
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
     * Get the menu for the service
     * @return Menu for the service
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
