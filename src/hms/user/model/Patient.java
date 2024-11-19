package hms.user.model;

import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a patient in the hospital management system.
 */
public class Patient extends User {
    // Extra info about patient's medical records
    /** The blood type of the Patient. */
    public String bloodType = "No info";
    /** The allergies of the Patient. */
    public String allergies = "No info";
    /** The medical history of the Patient. */
    public String medicalHistory = "No info";
    /** The current medication of the Patient. */
    public String currentMedication = "No info";

    /**
     * Constructs a new Patient with the specified ManagerContext.
     * @param ctx the ManagerContext to be used by the Patient
     */
    public Patient(ManagerContext ctx) {
        super(ctx);        
        this.account = new Account(IdManager.generateId(Patient.class));
    }

    /**
     * Constructs a new Patient with the specified details.
     * @param ctx the ManagerContext to be used by the Patient
     * @param name the name of the Patient
     * @param isMale the gender of the Patient
     * @param contact the contact information of the Patient
     * @param dob the date of birth of the Patient
     */
    public Patient(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
    }
      
    @Override
    public String toString() {
        return "Patient{" +
                "name='" + this.name + '\'' +
                ", isMale=" + this.isMale +
                ", contact='" + this.contact + '\'' +
                ", dob=" + this.dob +
                '}';
    }

    /**
     * {@inheritDoc}
     * Overrides the default implementation to include the extra fields of the Patient.
     */
    @Override
    public void hydrate(HashMap<String, String> data) {
        super.hydrate(data);
        this.bloodType = data.get("bloodType");
        this.allergies = data.get("allergies");
        this.medicalHistory = data.get("medicalHistory");
        this.currentMedication = data.get("currentMedication");
    }

    /**
     * {@inheritDoc}
     * Overrides the default implementation to include the extra fields of the Patient.
     */
    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = super.serialize();
        data.put("bloodType", this.bloodType);
        data.put("allergies", this.allergies);
        data.put("medicalHistory", this.medicalHistory);
        data.put("currentMedication", this.currentMedication);
        return data;
    }
}
