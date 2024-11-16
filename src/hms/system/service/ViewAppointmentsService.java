package hms.system.service;

import hms.appointment.Appointment;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.manager.PrescriptionManager;
import hms.manager.UserManager;
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
import hms.user.repository.DoctorRepository;
import hms.user.repository.PatientRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewAppointmentsService implements IService {
    /** The user using this service */
    private final User user;
    private final ManagerContext ctx;
    private final List<SearchCriterion<Appointment, ?>> defaultCriteria;

    private final UserOption cancelAppointmentOption = new UserOption("Cancel Appointment", this::handleCancelAppointment);
    private final UserOption completeAppointmentOption = new UserOption("Complete Appointment", this::handleCompleteAppointment);
    private final UserOption rescheduleAppointmentOption = new UserOption("Reschedule Appointment", this::handleRescheduleAppointment);
    private final UserOption resolveAppointmentOption = new UserOption("Accept Appointment", this::handleResolveAppointment);
    private final UserOption viewPatientInfoOption = new UserOption("View Patient Info", this::handleViewPatientInfo);
    private final UserOption viewRecordsOption = new UserOption("View Records", this::handleViewRecords);

    public boolean hasCancelAppointmentOption = false;
    public boolean hasCompleteAppointmentOption = false;
    public boolean hasRescheduleAppointmentOption = false;
    public boolean hasResolveAppointmentOption = false;
    public boolean hasViewPatientInfoOption = false;
    public boolean hasViewRecordsOption = false;

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

        // Update the appointment info display
        menuNav.getCurrentMenu().title = getAppointmentInfoDisplay(selected);
    }

    private void handleCancelAppointment() {
        Patient p = (Patient) user;

        p.cancelAppointment(selected.getId());

        // Update the entire menu
        menuNav.popMenu();
        onAppointmentSelect(selected);
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

    private void handleResolveAppointment() {
        Doctor d = (Doctor) user;

        boolean accepted = Prompt.getBooleanInput("Accept appointment? (y/n): ");

        d.resolvePendingAppointment(selected.getId(), accepted);

        // Update the entire menu
        menuNav.popMenu();
        onAppointmentSelect(selected);
    }

    private void handleCompleteAppointment() {
        String notes = Prompt.getStringInput("Enter notes for the appointment: ");
        notes = notes.replace("\n", " ");

        String service = Prompt.getStringInput("Enter services provided: ");

        PrescriptionManager prescriptionManager = ctx.getManager(PrescriptionManager.class);
        ArrayList<String> prescriptionIds = new ArrayList<>();

        while(Prompt.getBooleanInput("Add a prescription? (y/n): ")) {
            String medicalName = Prompt.getStringInput("Enter the medicine name: ");
            int quantity = Prompt.getIntInput("Enter the quantity: ");

            if(quantity <= 0) {
                System.out.println("Invalid quantity");
                continue;
            }

            Prescription p = prescriptionManager.createPrescription(medicalName, quantity);

            if(p == null) {
                System.out.println("Prescription not found");
                continue;
            }

            prescriptionIds.add(p.getId());
        }

        ctx.getManager(AppointmentManager.class)
            .resolveAppoinment(selected.getId(), service, prescriptionIds, notes);

        // Update the entire menu
        menuNav.popMenu();
        onAppointmentSelect(selected);
    }

    private void handleViewPatientInfo() {
        Patient p = ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .get(selected.getPatientId());

        String patientInfo = String.format(
            "Name: %s\nDOB: %s\nGender: %s\nContact Info: %s",
            p.name, p.dob, p.isMale ? "Male" : "Female", p.contact
        );

        SimpleMenu patientInfoMenu = new SimpleMenu(patientInfo, null);

        menuNav.addMenu(patientInfoMenu);
    }

    private String getAppointmentInfoDisplay(Appointment appointment){
        if(user instanceof Patient){
            Doctor d = ctx.getManager(UserManager.class)
                .getRepository(DoctorRepository.class)
                .get(appointment.getDoctorId());
    
            return String.format("Appointment with %s on %s\nStatus: %s",
                d.name, appointment.getDate(),
                appointment.getStatus().toString()
            );
        }
        
        if(user instanceof Doctor){
            Patient p = ctx.getManager(UserManager.class)
                .getRepository(PatientRepository.class)
                .get(appointment.getPatientId());

            return String.format("Appointment with %s on %s\nStatus: %s",
                p.name, appointment.getDate(),
                appointment.getStatus().toString()
            );
        }
        
        if(user instanceof Admin){
            Patient p = ctx.getManager(UserManager.class)
                .getRepository(PatientRepository.class)
                .get(appointment.getPatientId());

            Doctor d = ctx.getManager(UserManager.class)
                .getRepository(DoctorRepository.class)
                .get(appointment.getDoctorId());

            return String.format("Appointment by %s with %s on %s\nStatus: %s",
                p.name, d.name, appointment.getDate(),
                appointment.getStatus().toString()
            );
        }

        throw new IllegalStateException("Invalid user type");
    }

    private void onAppointmentSelect(Appointment appointment){
        this.selected = appointment;

        SimpleMenu appointmentMenu = new SimpleMenu(getAppointmentInfoDisplay(appointment), null);
        
        if(appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            if (hasCancelAppointmentOption) appointmentMenu.addOption(cancelAppointmentOption);
            if (hasCompleteAppointmentOption) appointmentMenu.addOption(completeAppointmentOption);
            if (hasRescheduleAppointmentOption) appointmentMenu.addOption(rescheduleAppointmentOption);
            if (hasViewPatientInfoOption) appointmentMenu.addOption(viewPatientInfoOption);
        }

        if(appointment.getStatus() == AppointmentStatus.PENDING) {
            if (hasCancelAppointmentOption) appointmentMenu.addOption(cancelAppointmentOption);
            if (hasRescheduleAppointmentOption) appointmentMenu.addOption(rescheduleAppointmentOption);
            if (hasResolveAppointmentOption) appointmentMenu.addOption(resolveAppointmentOption);
            if (hasViewPatientInfoOption) appointmentMenu.addOption(viewPatientInfoOption);
        }
        
        if(appointment.getStatus() == AppointmentStatus.FINISHED) {
            appointmentMenu.addOption(viewRecordsOption);
        }

        menuNav.addMenu(appointmentMenu);
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
