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
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.repository.DoctorRepository;
import hms.user.repository.PatientRepository;
import java.util.ArrayList;
import java.util.List;

public class ViewAppointmentRecordsServices implements IService {
    /** The user using this service */
    private final ManagerContext ctx;

    /** Bound menu navigator */
    private MenuNavigator menuNav;

    public boolean hasPrescriptionUpdateOption = false;

    private Appointment selected;

    /**
     * Constructor to initialize the service with the given context.
     * @param ctx The manager context.
     */
    public ViewAppointmentRecordsServices(ManagerContext ctx){
        this.ctx = ctx;
    }

    /**
     * Handles the update of a prescription record.
     */
    private void handleRecordPrescriptionUpdate() {
        ArrayList<String> prescriptionIds = selected.getRecord().getPrescriptions();

        int select = Prompt.getIntInput("Select a prescription to update: ");

        if(select < 0 || select >= prescriptionIds.size()){
            System.out.println("Invalid selection");
            return;
        }

        boolean confirm = Prompt.getBooleanInput("Are you sure you want to mark this prescription as fulfilled? (y/n)");

        if(!confirm) return;

        String prescriptionId = prescriptionIds.get(select);

        boolean result = ctx.getManager(PrescriptionManager.class)
            .updatePrescription(prescriptionId, true);

        if(!result){
            System.out.println("Failed to update prescription");
            System.out.println("There might not be enough stock to fulfill the prescription");
            return;
        }

        // Update display
        menuNav.getCurrentMenu().title = getRecordInfoDisplay(selected);
    }

    /**
     * Generates a display string for the given appointment record.
     * @param appointment The appointment to display.
     * @return The formatted string containing appointment details.
     */
    private String getRecordInfoDisplay(Appointment appointment){
        Patient p = ctx.getManager(UserManager.class)
            .getRepository(PatientRepository.class)
            .get(appointment.getPatientId());

        Doctor d = ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .get(appointment.getDoctorId());

        String recordInfo = "Appointment records for appointment" + appointment.getId() + "\n\n";
        recordInfo += "Patient " + appointment.getPatientId() + ":\n";
        recordInfo += "\tName: " + p.name + "\n";
        recordInfo += "\tDate of Birth: " + p.dob + "\n";
        recordInfo += "\tGender: " + (p.isMale ? "Male\n\n" : "Female\n\n");
        recordInfo += "Doctor " + appointment.getDoctorId() + ": " + d.name + "\n";
        recordInfo += "Appointment was on: " + appointment.getTimeslot() + "\n\n";
        recordInfo += "Notes: " + appointment.getRecord().getNotes() + "\n";
        recordInfo += "Service provided: " + appointment.getRecord().getService() + "\n";

        ArrayList<String> prescriptions = appointment.getRecord().getPrescriptions();

        if(prescriptions.isEmpty()) {
            recordInfo += "No prescriptions given";
            return recordInfo;
        }

        recordInfo += "Prescriptions given:";
        for(int i = 0; i < prescriptions.size(); i++){
            String prescriptionId = prescriptions.get(i);

            Prescription prescription = ctx.getManager(PrescriptionManager.class)
                .getPrescriptions(List.of(new SearchCriterion<>(Prescription::getId, prescriptionId)))
                .get(0);

            recordInfo += "\n\t" + i + ". " + prescription.getMedicalName() +
                " - " + prescription.getQuantity() +
                " - " + (prescription.getStatus() ? "Fulfilled" : "Not fulfilled");
        }

        return recordInfo;
    }

    /**
     * Handles the selection of an appointment record.
     * @param appointment The selected appointment.
     */
    private void onAppointmentRecordSelect(Appointment appointment){
        selected = appointment;

        SimpleMenu recordInfoMenu = new SimpleMenu(getRecordInfoDisplay(appointment), null);
        menuNav.addMenu(recordInfoMenu);

        if(appointment.getRecord().getPrescriptions().isEmpty()) return;

        if (hasPrescriptionUpdateOption){
            recordInfoMenu.addOption(new UserOption("Update prescription", this::handleRecordPrescriptionUpdate));
        }
    }

    /**
     * Generates the menu for viewing appointment records.
     * @return The generated menu.
     */
    private AbstractMenu getMenu() {
        Appointment[] appointments = ctx.getManager(AppointmentManager.class)
            .getAppointments(List.of(new SearchCriterion<>(Appointment::getStatus, AppointmentStatus.FINISHED)))
            .toArray(Appointment[]::new);

        if(appointments.length == 0){
            return new SimpleMenu("No appointments found", null);
        }
        
        PaginatedListSelector<Appointment> viewer = new PaginatedListSelector<>(
            "Select an appointment to view records", appointments,
            this::onAppointmentRecordSelect
        );

        viewer.sortBy((a) -> a.getTimeslot().getStartTime());

        return viewer;
    }
    
    /**
     * Executes the service and adds the menu to the navigator.
     * @param menuNav The menu navigator.
     */
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;
        
        menuNav.addMenu(getMenu());
    }
}
