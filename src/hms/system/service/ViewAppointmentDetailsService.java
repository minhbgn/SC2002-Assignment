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
import java.util.Date;
import java.util.List;

public class ViewAppointmentDetailsService implements IService{
    private final UserOption cancelAppointmentOption = new UserOption("Cancel Appointment", this::handleCancelAppointment);
    private final UserOption completeAppointmentOption = new UserOption("Complete Appointment", this::handleCompleteAppointment);
    private final UserOption rescheduleAppointmentOption = new UserOption("Reschedule Appointment", this::handleRescheduleAppointment);
    private final UserOption resolveAppointmentOption = new UserOption("Accept Appointment", this::handleResolveAppointment);
    private final UserOption updatePatientMedicalRecordOption = new UserOption("Update Patient Medical Record", this::handleUpdatePatientMedicalRecord);
    private final UserOption viewPatientInfoOption = new UserOption("View Patient Info", this::handleViewPatientInfo);
    private final UserOption viewPatientMedicalRecordsOption = new UserOption("View Patient Medical Records", this::handleViewPatientMedicalRecords);
    private final UserOption viewRecordsOption = new UserOption("View Records", this::handleViewRecords);

    public boolean hasCancelAppointmentOption = false;
    public boolean hasCompleteAppointmentOption = false;
    public boolean hasRescheduleAppointmentOption = false;
    public boolean hasResolveAppointmentOption = false;
    public boolean hasUpdatePatientMedicalRecordOption = false;
    public boolean hasViewPatientInfoOption = false;
    public boolean hasViewPatientMedicalRecordOption = false;
    public boolean hasViewRecordsOption = false;

    private final ManagerContext ctx;
    private final Appointment appointment;

    private MenuNavigator menuNav;

    public ViewAppointmentDetailsService(ManagerContext ctx, Appointment appointment){
        this.ctx = ctx;
        this.appointment = appointment;
    }

    private void handleCancelAppointment() {
        ctx.getManager(AppointmentManager.class)
            .updateStatus(appointment.getId(), AppointmentStatus.CANCELLED);

        // Update the entire menu
        menuNav.popMenu();
        menuNav.addMenu(getMenu());
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
            .resolveAppoinment(appointment.getId(), service, prescriptionIds, notes);

        // Update the entire menu
        menuNav.popMenu();
        menuNav.addMenu(getMenu());
    }
    
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

        freeTimeslotService.execute(menuNav);
    }

    private void handleResolveAppointment() {
        boolean accepted = Prompt.getBooleanInput("Accept appointment? (y/n): ");

        if(!accepted) return;

        ctx.getManager(AppointmentManager.class)
            .updateStatus(appointment.getId(), AppointmentStatus.ACCEPTED);

        // Update the entire menu
        menuNav.popMenu();
        menuNav.addMenu(getMenu());
    }

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

    private void handleViewPatientMedicalRecords() {
        Patient p = ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .get(appointment.getPatientId());

        String recordInfo = "Medical Records of " + p.name + ":\n\n" +
            "Blood Type: " + p.bloodType + "\n" +
            "Allergies: " + p.allergies + "\n" +
            "Medications: " + p.medicalHistory + "\n" +
            "Conditions: " + p.currentMedication;

        SimpleMenu recordMenu = new SimpleMenu(recordInfo, List.of(updatePatientMedicalRecordOption));

        menuNav.addMenu(recordMenu);
    }

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

    private AbstractMenu getMenu(){
        SimpleMenu appointmentMenu = new SimpleMenu(getAppointmentInfoDisplay(), null);
        
        if(appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            if (hasCancelAppointmentOption) appointmentMenu.addOption(cancelAppointmentOption);
            if (hasCompleteAppointmentOption) appointmentMenu.addOption(completeAppointmentOption);
            if (hasRescheduleAppointmentOption) appointmentMenu.addOption(rescheduleAppointmentOption);
            if (hasViewPatientInfoOption) appointmentMenu.addOption(viewPatientInfoOption);
            if (hasViewPatientMedicalRecordOption) appointmentMenu.addOption(viewPatientMedicalRecordsOption);
        }

        if(appointment.getStatus() == AppointmentStatus.PENDING) {
            if (hasCancelAppointmentOption) appointmentMenu.addOption(cancelAppointmentOption);
            if (hasRescheduleAppointmentOption) appointmentMenu.addOption(rescheduleAppointmentOption);
            if (hasResolveAppointmentOption) appointmentMenu.addOption(resolveAppointmentOption);
            if (hasViewPatientInfoOption) appointmentMenu.addOption(viewPatientInfoOption);
            if (hasViewPatientMedicalRecordOption) appointmentMenu.addOption(viewPatientMedicalRecordsOption);
        }
        
        if(appointment.getStatus() == AppointmentStatus.FINISHED) {
            if (hasViewRecordsOption) appointmentMenu.addOption(viewRecordsOption);
            if (hasViewPatientMedicalRecordOption) appointmentMenu.addOption(viewPatientMedicalRecordsOption);
        }

        return appointmentMenu;
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }

    /**
     * Utility method to get the display string for an appointment
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
}
