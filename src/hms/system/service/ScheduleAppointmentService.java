package hms.system.service;

import hms.appointment.Appointment;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.manager.UserManager;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.Prompt;
import hms.user.model.Doctor;
import hms.user.model.User;
import hms.user.repository.DoctorRepository;
import java.util.Date;

public class ScheduleAppointmentService implements IService {
    /** The user using this service */
    private final User user;
    private final ManagerContext ctx;

    /** Bound menu navigator */
    private MenuNavigator menuNav;

    public ScheduleAppointmentService(ManagerContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
    }

    private void onDoctorSelected(Doctor doctor){
        // Clear the screen
        System.out.println("\033[H\033[2J");

        System.out.println("Selected doctor: " + doctor);

        boolean confirm = Prompt.getBooleanInput("Would you like to " + 
            "schedule an appointment with this doctor? (y/n): ");
        if(!confirm) return;

        String doctorId = doctor.getAccount().getId();
        Date date = Prompt.getDateInput("Enter the date for your appointment: ");

        Appointment appointment = ctx
            .getManager(AppointmentManager.class)
            .makeAppointment(user.getAccount().getId(), doctorId, date);

        if(appointment == null) {
            System.out.println("Failed to schedule appointment");
            return;
        }

        System.out.println("Appointment scheduled: " + appointment);

        menuNav.popMenu();
    }


    public AbstractMenu getMenu(){
        Doctor[] doctors = ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .findWithFilters(null)
            .toArray(Doctor[]::new);

        PaginatedListSelector<Doctor> doctorSelector = new PaginatedListSelector<>(
            "Select a doctor for your appointment", doctors, this::onDoctorSelected
        );

        return doctorSelector;
    }
        
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }
    
}
