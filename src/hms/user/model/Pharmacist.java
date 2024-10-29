package hms.user.model;

import java.util.Date;

import hms.appointment.AppointmentRecord;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.prescription.Prescription;

public class Pharmacist extends User {
    public Pharmacist(ManagerContext ctx) {
        super(ctx);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Pharmacist(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public AppointmentRecord[] viewAppointmentRecords() {
        throw new UnsupportedOperationException("Unimplemented method 'viewAppointmentRecords'");
    }

    public void resolvePrescription(int prescriptionId) {
        throw new UnsupportedOperationException("Unimplemented method 'resolvePrescription'");
    }

    public Prescription[] viewPrescriptions(SearchCriterion<Prescription, ?>[] criteria) {
        throw new UnsupportedOperationException("Unimplemented method 'viewPrescriptions'");
    }

    public void viewStock(String medicalName) {
        throw new UnsupportedOperationException("Unimplemented method 'viewStock'");
    }

    public void requestRestock(String medicalName) {
        throw new UnsupportedOperationException("Unimplemented method 'requestRestock'");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Unimplemented method 'toString'");
    }
}
