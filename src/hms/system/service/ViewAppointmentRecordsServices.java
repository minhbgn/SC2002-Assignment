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

    public ViewAppointmentRecordsServices(ManagerContext ctx){
        this.ctx = ctx;
    }

    private void handleRecordPrescriptionUpdate() {
        ArrayList<String> prescriptionIds = selected.getRecord().getPrescriptions();

        int select = Prompt.getIntInput("Select a prescription to update: ");

        if(select < 0 || select >= prescriptionIds.size()){
            System.out.println("Invalid selection");
            return;
        }

        boolean isFulfilled = Prompt.getBooleanInput("Is the prescription fulfilled? (y/n): ");

        String prescriptionId = prescriptionIds.get(select);

        ctx.getManager(PrescriptionManager.class)
            .updatePrescription(prescriptionId, isFulfilled);
    }

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
        recordInfo += "Appointment was on: " + appointment.getDate() + "\n\n";
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

    private void onAppointmentRecordSelect(Appointment appointment){
        selected = appointment;

        SimpleMenu recordInfoMenu = new SimpleMenu(getRecordInfoDisplay(appointment), null);
        menuNav.addMenu(recordInfoMenu);

        if(appointment.getRecord().getPrescriptions().isEmpty()) return;

        if (hasPrescriptionUpdateOption){
            recordInfoMenu.addOption(new UserOption("Update prescription", this::handleRecordPrescriptionUpdate));
        }
    }

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

        viewer.sortBy((a) -> a.getDate());

        return viewer;
    }
    
    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;
        
        menuNav.addMenu(getMenu());
    }
}
