package hms.system.service;

import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.SimpleMenu;
import hms.user.model.Patient;

public class ViewRecordsService implements IService {
    private final Patient patient;

    public ViewRecordsService(Patient patient) {
        this.patient = patient;
    }

    private String getMedicalRecordsDisplay(){
        return "Medical Records of " + patient.name + ":\n" +
            "Blood Type: " + patient.bloodType + "\n" +
            "Allergies: " + patient.allergies + "\n" +
            "Medications: " + patient.medicalHistory + "\n" +
            "Conditions: " + patient.currentMedication;
    }

    private AbstractMenu getMenu(){
        return new SimpleMenu(getMedicalRecordsDisplay(), null);
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        menuNav.addMenu(getMenu());
    }
}
