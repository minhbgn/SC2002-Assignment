package hms.system.service;

import hms.appointment.Appointment;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.Prompt;
import hms.user.model.Doctor;
import hms.utils.Timeslot;
import java.util.Date;
import java.util.List;

public class ViewUpcomingAppointmentService implements IService{

    private final ManagerContext ctx;
    private final List<SearchCriterion<Appointment,?>> defaultCriteria;
    private int upcomingDays;
    private MenuNavigator menuNav;

    public ViewUpcomingAppointmentService(ManagerContext ctx, Doctor doctor){
        this.ctx = ctx;
        this.defaultCriteria = List.of(
            new SearchCriterion<>(Appointment::getDoctorId, doctor.getAccount().getId()),
            new SearchCriterion<>(Appointment::getStatus, AppointmentStatus.ACCEPTED)
        );
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

        long now = System.currentTimeMillis();
        long end = now + (long) upcomingDays * 24 * 60 * 60 * 1000;
        Timeslot upcomingTimeslot = new Timeslot(
            new Date(now),
            new Date(end)
        );

        System.out.println(upcomingTimeslot);

        appointments.removeIf(appointment -> !upcomingTimeslot.isOverlapping(appointment.getTimeslot()));

        PaginatedListSelector<Appointment> selector = new PaginatedListSelector<>(
            "Upcoming Appointments", appointments.toArray(Appointment[]::new),
            this::onAppointmentSelected
        );

        return selector;
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        upcomingDays = Prompt.getIntInput("Enter the number of upcoming days to view appointments for: ");

        menuNav.addMenu(getMenu());
    }
}
