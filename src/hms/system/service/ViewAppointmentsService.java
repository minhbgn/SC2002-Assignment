package hms.system.service;

import hms.appointment.Appointment;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.manager.PrescriptionManager;
import hms.prescription.Prescription;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Admin;
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewAppointmentsService implements IService {
    /** The user using this service */
    private final User user;
    private final ManagerContext ctx;
    private final List<SearchCriterion<Appointment, ?>> defaultCriteria;

    private final UserOption rescheduleAppointmentOption = new UserOption("Reschedule Appointment", this::handleRescheduleAppointment);
    private final UserOption cancelAppointmentOption = new UserOption("Cancel Appointment", this::handleCancelAppointment);
    private final UserOption viewRecordsOption = new UserOption("View Records", this::handleViewRecords);

    /** Variable for keeping track of the selected appointment */
    private Appointment selected;
    /** Bound menu navigator */
    private MenuNavigator menuNav;

    public ViewAppointmentsService(
        ManagerContext ctx, User user,
        List<SearchCriterion<Appointment, ?>> defaultCriteria) {
        
        this.ctx = ctx;
        this.user = user;
        this.defaultCriteria = defaultCriteria;
    }

    private void handleRescheduleAppointment() {
        Patient p = (Patient) user;

        Date newDate = Prompt.getDateInput("Enter the new date for your appointment: ");
        p.rescheduleAppointment(selected.getId(), newDate);
    }

    private void handleCancelAppointment() {
        Patient p = (Patient) user;

        p.cancelAppointment(selected.getId());
    }

    private void handleViewRecords() {
        String recordInfo = String.format(
            "Notes: %s\nServices: %s",
            selected.getRecord().getNotes(),
            selected.getRecord().getService()
        );

        ArrayList<String> prescriptions = selected.getRecord().getPrescriptions();

        if(!prescriptions.isEmpty()) {
            recordInfo += "\nPrescriptions: ";
            PrescriptionManager prescriptionManager = ctx.getManager(PrescriptionManager.class);

            for(String prescription : prescriptions) {
                Prescription p = prescriptionManager.getPrescriptions(List.of(
                    new SearchCriterion<>(Prescription::getId, prescription)
                )).get(0);

                if(p != null) {
                    recordInfo += "\n\t" + p;
                }
            }
        }

        SimpleMenu recordMenu = new SimpleMenu(recordInfo, null);

        menuNav.addMenu(recordMenu);
    }

    private void onAppointmentSelect(Appointment appointment){
        this.selected = appointment;

        if(user instanceof Patient) {
            menuNav.addMenu(getAppointmentSelectPatientMenu(appointment));
            return;
        }

        if(user instanceof Doctor) {
            menuNav.addMenu(getAppointmentSelectDoctorMenu(appointment));
            return;
        }

        if(user instanceof Admin) {
            menuNav.addMenu(getAppointmentSelectAdminMenu(appointment));
            return;
        }

        throw new UnsupportedOperationException("User type not supported: " + user.getClass().getName());
    }

    private AbstractMenu getAppointmentSelectPatientMenu(Appointment appointment){
        String appointmentInfo = String.format(
            "Appointment with %s on %s\nStatus: %s",
            appointment.getDoctorId(), appointment.getDate(),
            appointment.getStatus().toString()
        );

        SimpleMenu appointmentMenu = new SimpleMenu(appointmentInfo, null);

        if(appointment.getStatus() == AppointmentStatus.PENDING
            || appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            appointmentMenu.addOption(cancelAppointmentOption);
            appointmentMenu.addOption(rescheduleAppointmentOption);
        }

        if(appointment.getStatus() == AppointmentStatus.FINISHED) {
            appointmentMenu.addOption(viewRecordsOption);
        }

        return appointmentMenu;
    }

    private AbstractMenu getAppointmentSelectDoctorMenu(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private AbstractMenu getAppointmentSelectAdminMenu(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AbstractMenu getMenu() {
        List<Appointment> appointments = ctx.getManager(AppointmentManager.class)
            .getAppointments(defaultCriteria);

        if (appointments.isEmpty()) return new SimpleMenu("No appointments found.", null);
        
        PaginatedListSelector<Appointment> viewer = new PaginatedListSelector<>(
            "Appointments", appointments.toArray(Appointment[]::new),
            this::onAppointmentSelect
        );

        return viewer;
    }
    
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;
        
        menuNav.addMenu(getMenu());
    }
}
