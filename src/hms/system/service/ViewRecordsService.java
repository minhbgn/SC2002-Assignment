package hms.system.service;

import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.SimpleMenu;
import hms.user.model.Patient;

public class ViewRecordsService implements IService {
    private final Patient patient;

    /**
     * Constructs a ViewRecordsService with the specified patient.
     *
     * @param patient the patient whose records are to be viewed
     */
    public ViewRecordsService(Patient patient) {
        this.patient = patient;
    }

    /**
     * Retrieves the medical records display string for the patient.
     *
     * @return a string representation of the patient's medical records
     */
    private String getMedicalRecordsDisplay(){
        return "Medical Records of " + patient.name + ":\n" +
            "Blood Type: " + patient.bloodType + "\n" +
            "Allergies: " + patient.allergies + "\n" +
            "Medications: " + patient.medicalHistory + "\n" +
            "Conditions: " + patient.currentMedication;
    }

    /**
     * Creates a menu displaying the patient's medical records.
     *
     * @return an AbstractMenu displaying the patient's medical records
     */
    private AbstractMenu getMenu(){
        return new SimpleMenu(getMedicalRecordsDisplay(), null);
    }

    /**
     * Executes the service by adding the medical records menu to the menu navigator.
     *
     * @param menuNav the menu navigator to which the menu is added
     */
    @Override
    public void execute(MenuNavigator menuNav) {
        menuNav.addMenu(getMenu());
    }
}
