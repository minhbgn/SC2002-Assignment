package hms.appointment;

import java.util.ArrayList;
import java.util.HashMap;

import hms.common.IModel;
import hms.prescription.Prescription;

public class AppointmentRecord implements IModel {
    private String service;
    private ArrayList<Prescription> prescriptions;
    private String notes;

    AppointmentRecord() {
    }

    AppointmentRecord(String service, ArrayList<Prescription> prescriptions, String notes) {
        this.service = service;
        this.prescriptions = prescriptions;
        this.notes = notes;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getService() {
        return service;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hydrate'");
    }

    @Override
    public HashMap<String, String> serialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }
}
