package hms.system.service;

import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.SimpleMenu;
import hms.user.model.Patient;

/** Service to view the medical records of a patient. */
public class ViewRecordsService implements IService {
    /** The patient whose records are to be viewed. */
    private final Patient patient;

    /**
     * Creates a new ViewRecordsService.
     * @param patient The patient whose records are to be viewed.
     */
    public ViewRecordsService(Patient patient) {
        this.patient = patient;
    }

    /**
     * Returns a string representation of the patient's medical records.
     * @return A string representation of the patient's medical records.
     */
    private String getMedicalRecordsDisplay(){
        return "Medical Records of " + patient.name + ":\n" +
            "Blood Type: " + patient.bloodType + "\n" +
            "Allergies: " + patient.allergies + "\n" +
            "Medications: " + patient.medicalHistory + "\n" +
            "Conditions: " + patient.currentMedication;
    }

    /**
     * Returns a menu that displays the patient's medical records.
     * @return A menu that displays the patient's medical records.
     */
    private AbstractMenu getMenu(){
        return new SimpleMenu(getMedicalRecordsDisplay(), null);
    }

    /**
     * Adds the menu that displays the patient's medical records to the menu navigator.
     * @param menuNav The menu navigator to which the menu is to be added.
     */
    @Override
    public void execute(MenuNavigator menuNav) {
        menuNav.addMenu(getMenu());
    }
}
