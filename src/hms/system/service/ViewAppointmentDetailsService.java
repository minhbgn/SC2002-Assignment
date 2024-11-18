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
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.repository.DoctorRepository;
import hms.user.repository.PatientRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to view the details of an appointment
 */
public class ViewAppointmentDetailsService implements IService{
    // Options for the menu
    /** Option to cancel the appointment */
    private final UserOption cancelAppointmentOption = new UserOption("Cancel Appointment", this::handleCancelAppointment);

    /** Option to complete the appointment */
    private final UserOption completeAppointmentOption = new UserOption("Complete Appointment", this::handleCompleteAppointment);

    /** Option to reschedule the appointment */
    private final UserOption rescheduleAppointmentOption = new UserOption("Reschedule Appointment", this::handleRescheduleAppointment);

    /** Option to resolve the appointment */
    private final UserOption resolveAppointmentOption = new UserOption("Accept Appointment", this::handleResolveAppointment);

    /** Option to update the patient's medical record */
    private final UserOption updatePatientMedicalRecordOption = new UserOption("Update Patient Medical Record", this::handleUpdatePatientMedicalRecord);

    /** Option to view the patient's personal information */
    private final UserOption viewPatientInfoOption = new UserOption("View Patient Info", this::handleViewPatientInfo);

    /** Option to view the patient's medical records */
    private final UserOption viewPatientMedicalRecordsOption = new UserOption("View Patient Medical Records", this::handleViewPatientMedicalRecords);

    /** Option to view the appointment records */
    private final UserOption viewRecordsOption = new UserOption("View Records", this::handleViewRecords);

    // Flags to determine which options to show
    /** Flag to determine if the cancel appointment option should be shown */
    public boolean hasCancelAppointmentOption = false;
    /** Flag to determine if the complete appointment option should be shown */
    public boolean hasCompleteAppointmentOption = false;
    /** Flag to determine if the reschedule appointment option should be shown */
    public boolean hasRescheduleAppointmentOption = false;
    /** Flag to determine if the resolve appointment option should be shown */
    public boolean hasResolveAppointmentOption = false;
    /** Flag to determine if the update patient medical record option should be shown */
    public boolean hasUpdatePatientMedicalRecordOption = false;
    /** Flag to determine if the view patient info option should be shown */
    public boolean hasViewPatientInfoOption = false;
    /** Flag to determine if the view patient medical records option should be shown */
    public boolean hasViewPatientMedicalRecordOption = false;
    /** Flag to determine if the view records option should be shown */
    public boolean hasViewRecordsOption = false;

    /** The manager context */
    private final ManagerContext ctx;
    /** The appointment to view */
    private final Appointment appointment;

    /** The menu navigator */
    private MenuNavigator menuNav;

    /**
     * Constructor for the service
     * @param ctx The manager context
     * @param appointment The appointment to view
     */
    public ViewAppointmentDetailsService(ManagerContext ctx, Appointment appointment){
        this.ctx = ctx;
        this.appointment = appointment;
    }

    /** Handler for the cancel appointment option */
    private void handleCancelAppointment() {
        ctx.getManager(AppointmentManager.class)
            .updateStatus(appointment.getId(), AppointmentStatus.CANCELLED);

        // Update the entire menu
        menuNav.popMenu();
        menuNav.addMenu(getMenu());
    }

    /** Handler for the complete appointment option */
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
            .resolveAppoinment(appointment.getId(), service, prescriptionIds, notes);

        // Update the entire menu
        menuNav.popMenu();
        menuNav.addMenu(getMenu());
    }
    
    /** Handler for the reschedule appointment option */
    private void handleRescheduleAppointment() {
        QueryFreeTimeslotService freeTimeslotService = new QueryFreeTimeslotService(ctx, (timeslot) -> {
            // Update the timeslot of the appointment
            boolean success = ctx.getManager(AppointmentManager.class)
                .updateTimeslot(appointment.getId(), timeslot);
        
            if(!success) // This should never happen
                throw new RuntimeException("Failed to update appointment timeslot");
                
            // Update the appointment info display
            menuNav.getCurrentMenu().title = getAppointmentInfoDisplay();
        });

        freeTimeslotService.bindDoctor(appointment.getDoctorId());
        freeTimeslotService.bindUser(appointment.getPatientId());
    }

    /** Handler for the resolve appointment option */
    private void handleResolveAppointment() {
        boolean accepted = Prompt.getBooleanInput("Accept appointment? (y/n): ");

        if(!accepted) return;

        ctx.getManager(AppointmentManager.class)
            .updateStatus(appointment.getId(), AppointmentStatus.ACCEPTED);

        // Update the entire menu
        menuNav.popMenu();
        menuNav.addMenu(getMenu());
    }

    /** Handler for the update patient medical record option */
    private void handleUpdatePatientMedicalRecord() {
        Patient p = ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .get(appointment.getPatientId());

        System.out.print("\033[H\033[2J");
        if(Prompt.getBooleanInput("Update blood type? (y/n): ")){
            System.out.println("Current data: " + p.bloodType);
            p.bloodType = Prompt.getStringInput("Enter the updated blood type: ");
        }
        
        if(Prompt.getBooleanInput("Update allergies? (y/n): ")){
            System.out.println("Current data: " + p.allergies);
            p.allergies = Prompt.getStringInput("Enter the updated allergies: ");
        }
        
        if(Prompt.getBooleanInput("Update medical history? (y/n): ")){
            System.out.println("Current data: " + p.medicalHistory);
            p.medicalHistory = Prompt.getStringInput("Enter the updated medical history: ");
        }
        
        if(Prompt.getBooleanInput("Update current medication? (y/n): ")){
            System.out.println("Current data: " + p.currentMedication);
            p.currentMedication = Prompt.getStringInput("Enter the updated current medication: ");
        }

        // Update the entire menu
        menuNav.popMenu();
        handleViewPatientMedicalRecords(); // Return to the patient medical records menu
    }

    /** Handler for the view patient info option */
    private void handleViewPatientInfo() {
        Patient p = ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .get(appointment.getPatientId());

        String patientInfo = String.format(
            "Name: %s\nDOB: %s\nGender: %s\nContact Info: %s",
            p.name, p.dob, p.isMale ? "Male" : "Female", p.contact
        );

        SimpleMenu patientInfoMenu = new SimpleMenu(patientInfo, null);

        if (hasUpdatePatientMedicalRecordOption) patientInfoMenu.addOption(updatePatientMedicalRecordOption);

        menuNav.addMenu(patientInfoMenu);
    }

    /** Handler for the view patient medical records option */
    private void handleViewPatientMedicalRecords() {
        Patient p = ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .get(appointment.getPatientId());

        String recordInfo = "Medical Records of " + p.name + ":\n\n" +
            "Blood Type: " + p.bloodType + "\n" +
            "Allergies: " + p.allergies + "\n";

        if(Prompt.getBooleanInput("Update current medication? (y/n): ")){
            System.out.println("Current data: " + p.currentMedication);
            p.currentMedication = Prompt.getStringInput("Enter the updated current medication: ");
        }

        // Update the entire menu
        menuNav.popMenu();
        handleViewPatientMedicalRecords(); // Return to the patient medical records menu
    }

    /** Handler for the view records option */
    private void handleViewRecords() {
        String recordInfo = String.format(
            "Notes: %s\nServices: %s",
            appointment.getRecord().getNotes(),
            appointment.getRecord().getService()
        );

        ArrayList<String> prescriptions = appointment.getRecord().getPrescriptions();

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

    /**
     * Utility method to get the menu for the appointment
     * @return The menu for the appointment
     */
    private AbstractMenu getMenu(){
        SimpleMenu appointmentMenu = new SimpleMenu(getAppointmentInfoDisplay(), null);

        if(appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            if (hasCancelAppointmentOption) appointmentMenu.addOption(cancelAppointmentOption);
            if (hasCompleteAppointmentOption) appointmentMenu.addOption(completeAppointmentOption);
            if (hasRescheduleAppointmentOption) appointmentMenu.addOption(rescheduleAppointmentOption);
            if (hasViewPatientInfoOption) appointmentMenu.addOption(viewPatientInfoOption);
        }

        return appointmentMenu;
    }

    /**
     * Executes the service by adding the appointment details menu to the navigator.
     * 
     * @param menuNav the menu navigator for adding menus
     */
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }

    /**
     * Utility method to get the display string for an appointment.
     * 
     * @return The display string for the appointment
     */
    private String getAppointmentInfoDisplay(){
        Patient p = ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .get(appointment.getPatientId());

        Doctor d = ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .get(appointment.getDoctorId());

        return String.format("Appointment by %s with %s from %s to %s\nStatus: %s",
            p.name, d.name,
            appointment.getTimeslot().getStartTimeString(),
            appointment.getTimeslot().getEndTimeString(),
            appointment.getStatus().toString()
        );
    }

    /**
     * Handles the process of viewing appointment details.
     */
    public void viewAppointmentDetails() {
        // Existing code...

        while (true) {
            // Existing code...

            Prescription p = prescriptionManager.createPrescription(medicalName, quantity);

            if (p == null) {
                System.out.println("Prescription not found");
                continue;
            }

            prescriptionIds.add(p.getId());
        }

        ctx.getManager(AppointmentManager.class)
            .resolveAppoinment(appointment.getId(), service, prescriptionIds, notes);

        // Update the entire menu
        menuNav.popMenu();
        menuNav.addMenu(getMenu());
    }
}
