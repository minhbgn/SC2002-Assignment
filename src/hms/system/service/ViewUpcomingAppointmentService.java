package hms.system.service;

import hms.appointment.Appointment;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.PaginatedListViewer;
import hms.ui.Prompt;
import hms.ui.UserOption;
import hms.user.model.Doctor;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewUpcomingAppointmentService implements IService{
    private final UserOption addBusyTimeslotOption = new UserOption("Add Busy Timeslot", this::handleAddBusyTimeslot);
    private final UserOption viewAvailableTimeslotsOption = new UserOption("View Available Timeslots", this::handleViewAvailableTimeslots);

    private final ManagerContext ctx;
    private final List<SearchCriterion<Appointment,?>> defaultCriteria;
    private final Doctor doctor;
    
    private int upcomingDays;
    private MenuNavigator menuNav;

    public ViewUpcomingAppointmentService(ManagerContext ctx, Doctor doctor){
        this.ctx = ctx;
        this.doctor = doctor;
        this.defaultCriteria = List.of(
            new SearchCriterion<>(Appointment::getDoctorId, doctor.getAccount().getId()),
            new SearchCriterion<>(Appointment::getStatus, AppointmentStatus.ACCEPTED)
        );
    }

    private void handleAddBusyTimeslot(){
        Date startTime = Prompt.getDetailedDateInput("When are you not available? (Start Time): ");
        Date endTime = Prompt.getDetailedDateInput("When will you be available again? (End Time): ");

        Timeslot busyTimeslot = new Timeslot(startTime, endTime);

        doctor.addBusyTimeslot(busyTimeslot);
    }

    private void handleViewAvailableTimeslots(){
        List<Timeslot> availableTimeslots = new ArrayList<>();

        for (int i = 0; i < upcomingDays; i++){
            Timeslot timeslot = calculateWorkingHourTimeslot(i);

            Timeslot[] free = ctx.getManager(AppointmentManager.class).
                getAllFreeTimeslots(null, doctor.getAccount().getId(), timeslot);

            availableTimeslots.addAll(Arrays.asList(free));
        }

        availableTimeslots.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));

        PaginatedListViewer<Timeslot> viewer = new PaginatedListViewer<>(
            "Available Timeslots", availableTimeslots.toArray(Timeslot[]::new)
        );

        menuNav.addMenu(viewer);
    }

    private void onAppointmentSelected(Appointment appointment){
        ViewAppointmentDetailsService service = new ViewAppointmentDetailsService(ctx, appointment);

        service.hasViewPatientInfoOption = true;
        service.hasViewPatientMedicalRecordOption = true;
        service.hasUpdatePatientMedicalRecordOption = true;
        service.hasCompleteAppointmentOption = true;

        service.execute(menuNav);
    }

    private AbstractMenu getMenu(){
        List<Appointment> appointments = ctx.getManager(AppointmentManager.class).getAppointments(defaultCriteria);

        Timeslot upcomingTimeslot = calculateUpcomingDaysTimeslot();

        appointments.removeIf(appointment -> !upcomingTimeslot.isOverlapping(appointment.getTimeslot()));

        PaginatedListSelector<Appointment> selector = new PaginatedListSelector<>(
            "Upcoming Appointments", appointments.toArray(Appointment[]::new),
            this::onAppointmentSelected
        );

        selector.addOption("a", addBusyTimeslotOption);
        selector.addOption("v", viewAvailableTimeslotsOption);

        return selector;
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        upcomingDays = Prompt.getIntInput("Enter the number of upcoming days to view appointments for: ");

        menuNav.addMenu(getMenu());
    }

    /**
     * Utility method for calculating the timeslot for the upcoming days.
     * @return The timeslot for the upcoming days from now to the end of the last day
     * specified by the upcomingDays field.
     */
    private Timeslot calculateUpcomingDaysTimeslot() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(new Date());
        Date now = calendar.getTime();

        calendar.add(Calendar.DATE, upcomingDays);
        // Round to the end of the day
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date end = calendar.getTime();

        Timeslot upcomingTimeslot = new Timeslot(now, end);
        return upcomingTimeslot;
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
        
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        Date dayStart = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 19);
        Date dayEnd = calendar.getTime();

        return new Timeslot(dayStart, dayEnd);
    }
}
