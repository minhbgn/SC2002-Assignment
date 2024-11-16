package hms.system.service;

import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.manager.UserManager;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Service for querying free timeslots for the user to select from
 * This service is not meant to be used directly, but rather to be used by other services
 */
public class QueryFreeTimeslotService implements IService {
    /** The start of the working hours in a day */
    private static final int WORKING_HOURS_START = 7;
    /** The end of the working hours in a day */
    private static final int WORKING_HOURS_END = 19;
    /** The minimum duration of an appointment in minutes */
    private static final int MINIMUM_APPOINTMENT_DURATION_IN_MINUTES = 30;
    /**
     * The start of the schedule period in days from now. </p>
     * 
     * The schedule period is the number of days from now
     * that the user can schedule an appointment. </p>
     * 
     * For example, if the schedule period start is 7,
     * the user can schedule an appointment starting from 7 days from now. </p>
     */
    private static final int SCHEDULE_PERIOD_START = 7;
    /**
     * The end of the schedule period in days from now. </p>
     * 
     * The schedule period is the number of days from now
     * that the user can schedule an appointment. </p>
     * 
     * For example, if the schedule period end is 30,
     * the user can schedule an appointment up to 30 days from now. </p>
     */
    private static final int SCHEDULE_PERIOD_END = 30;

    /** The manager context */
    private final ManagerContext ctx;
    /** Callback for when the user successfully finds a free timeslot */    
    private final Consumer<Timeslot> onTimeslotSelect;

    /** Id of the user making the appointment */
    private String userId;
    /** Id of the doctor being scheduled with */
    private String doctorId;
    /** List of free timeslots for the user to select from */
    private List<Timeslot> freeTimeslots;
    /** The selected timeslot which does not have any appointment */
    private Timeslot selectedFreeTimeslot;
    /** The selected start time for the appointment */
    private Date selectedStartTime;
    /** Bound menu navigator */
    private MenuNavigator menuNav;

    /**
     * Create a new QueryFreeTimeslotService
     * @param ctx The manager context
     * @param onTimeslotSelect The callback for when the user successfully finds a free timeslot
     */
    QueryFreeTimeslotService(ManagerContext ctx, Consumer<Timeslot> onTimeslotSelect) {
        this.ctx = ctx;
        this.onTimeslotSelect = onTimeslotSelect;
    }

    /** Bind the doctor id to the service */
    public void bindDoctor(String doctorId) { this.doctorId = doctorId; }
    /** Bind the user id to the service */
    public void bindUser(String userId) { this.userId = userId; }

    /**
     * Callback for when the user selects a start time for the appointment.
     * This will prompt the user to select an end time for the appointment.
     * @param startTime The selected start time for the appointment
     */
    private void onSelectAppointmentStartTime(Date startTime) {
        menuNav.popMenu(); // Pop the start time selector
        
        selectedStartTime = startTime;
        selectedFreeTimeslot = findTimeslotContainingDate(startTime);

        List<Date> availableEndTimes = calculateAvailableEndTimes();

        PaginatedListSelector<Date> appointmentEndDateSelector = new PaginatedListSelector<>(
            "Select a new end time for the appointment", availableEndTimes.toArray(Date[]::new),
            this::onSelectAppointmentEndTime
        );

        menuNav.addMenu(appointmentEndDateSelector);
    }

    /**
     * Callback for when the user selects an end time for the appointment.
     * This will finalize the process of finding a free timeslot for the appointment.
     * @param endTime The selected end time for the appointment
     */
    private void onSelectAppointmentEndTime(Date endTime) {
        menuNav.popMenu(); // Pop the end time selector

        // Create the selected timeslot
        Timeslot selectedTimeslot = new Timeslot(selectedStartTime, endTime);
        
        // Call the callback
        onTimeslotSelect.accept(selectedTimeslot);
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        // Bind the menu navigator
        this.menuNav = menuNav;

        // Update the free timeslots,
        // This requires the doctorId and userId to be set
        updateFreeTimeslots();

        List<Date> availableStartTimes = calculateAvailableStartTimes();

        PaginatedListSelector<Date> appointmentStartDateSelector = new PaginatedListSelector<>(
            "Select a new start time for the appointment", availableStartTimes.toArray(Date[]::new),
            this::onSelectAppointmentStartTime
        );

        menuNav.addMenu(appointmentStartDateSelector);
    }
    
    /** Utility method for updating the free timeslots for the user to select from */
    private void updateFreeTimeslots(){
        freeTimeslots = new ArrayList<>();

        if(doctorId == null || userId == null)
            throw new RuntimeException("Doctor and user must be set before updating free timeslots");

        if(!ctx.getManager(UserManager.class).hasUser(userId))
            throw new RuntimeException("Invalid user id");

        if(!ctx.getManager(UserManager.class).hasUser(doctorId))
            throw new RuntimeException("Invalid doctor id");

        for(int i = SCHEDULE_PERIOD_START; i <= SCHEDULE_PERIOD_END; i++) {
            Timeslot timeslot = calculateWorkingHourTimeslot(i);
            
            freeTimeslots.addAll(List.of(
                ctx.getManager(AppointmentManager.class)
                .getAllFreeTimeslots(userId, doctorId, timeslot)
            ));
        }
    }

    /** Utility method for finding the timeslot that contains the selected time */
    private Timeslot findTimeslotContainingDate(Date time) {
        // Find the timeslot that contains the selected start time
        Timeslot selectedTimeslot = null;
        for(Timeslot timeslot : freeTimeslots) {
            if(time.after(timeslot.getStartTime()) && time.before(timeslot.getEndTime())) {
                selectedTimeslot = timeslot;
                break;
            }
        }

        // This should never happen
        if(selectedTimeslot == null) throw new RuntimeException("Invalid timeslot");

        return selectedTimeslot;
    }

    /**
     * Utility method for calculating the working hours for a given day which is a number of days from now
     * @param daysFromNow The number of days from now, this will be used to calculate the working hours for that day
     * @return The working hours for the given day
     */
    private Timeslot calculateWorkingHourTimeslot(int daysFromNow){
        // Calculate the working hours for the given day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, daysFromNow);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        calendar.set(Calendar.HOUR_OF_DAY, WORKING_HOURS_START);
        Date dayStart = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, WORKING_HOURS_END);
        Date dayEnd = calendar.getTime();

        return new Timeslot(dayStart, dayEnd);
    }


    /** Utility method for calculating the available start times for the user to select from */
    private List<Date> calculateAvailableStartTimes() {
        List<Date> availableStartTimes = new ArrayList<>();

        for(Timeslot timeslot : freeTimeslots) {
            Date startTime = timeslot.getStartTime();
            // startTime < timeslot.getEndTime()
            // This is to ensure the appointment can be scheduled meaningfully (i.e. duration != 0)
            while (startTime.before(timeslot.getEndTime())) {
                // Add the start time to the list of available start times
                availableStartTimes.add(startTime);

                // The next start time should be at least
                // MINIMUM_APPOINTMENT_DURATION_IN_MINUTES
                // after the current start time
                startTime = new Date(startTime.getTime() + MINIMUM_APPOINTMENT_DURATION_IN_MINUTES * 60 * 1000);
            }
        }

        return availableStartTimes;
    }

    /** Utility method for calculating the available end times for the user to select from */
    private List<Date> calculateAvailableEndTimes() {
        List<Date> availableEndTimes = new ArrayList<>();

        // The end time should be at least MINIMUM_APPOINTMENT_DURATION_IN_MINUTES after the start time
        Date appointmentEndTime = new Date(selectedStartTime.getTime()
            + MINIMUM_APPOINTMENT_DURATION_IN_MINUTES * 60 * 1000);

        // endTime <= selectedFreeTimeslot.getEndTime()
        // This is because the appointment the appointment can end
        // right before the next appointment starts
        while (appointmentEndTime.before(selectedFreeTimeslot.getEndTime()) ||
            appointmentEndTime.equals(selectedFreeTimeslot.getEndTime())) {
            
            availableEndTimes.add(appointmentEndTime);
            appointmentEndTime = new Date(appointmentEndTime.getTime()
                + MINIMUM_APPOINTMENT_DURATION_IN_MINUTES * 60 * 1000);
        }

        return availableEndTimes;
    }
}
