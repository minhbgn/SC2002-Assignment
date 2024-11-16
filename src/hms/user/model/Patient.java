package hms.user.model;
import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import java.util.Date;
import java.util.HashMap;

public class Patient extends User {
    // Extra info about patient's medical records
    public String bloodType = "No info";
    public String allergies = "No info";
    public String medicalHistory = "No info";
    public String currentMedication = "No info";

    public Patient(ManagerContext ctx) {
        super(ctx);
        
        this.account = new Account(IdManager.generateId(Patient.class));
    }

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

    @Override
    public void hydrate(HashMap<String, String> data) {
        super.hydrate(data);
        this.bloodType = data.get("bloodType");
        this.allergies = data.get("allergies");
        this.medicalHistory = data.get("medicalHistory");
        this.currentMedication = data.get("currentMedication");
    }

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
