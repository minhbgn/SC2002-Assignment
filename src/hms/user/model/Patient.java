package hms.user.model;
import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import java.util.Date;

public class Patient extends User {
    // Extra info about patient's medical records
    public String bloodType = "None Specified";
    public String allergies = "None Specified";
    public String medicalHistory = "None Specified";
    public String currentMedication = "None Specified";

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
}
