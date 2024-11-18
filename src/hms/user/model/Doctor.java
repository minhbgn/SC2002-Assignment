package hms.user.model;

import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/** Doctor class to represent the Doctor user type */
public class Doctor extends User {
    /** Delimiter for serializing busy timeslots */
    private static String DELIMITER = ";";
    /** List of busy timeslots for the Doctor */
    private final List<Timeslot> busyTimeslots = new ArrayList<>();

	/**
	 * Empty constructor to be used for hydration
	 * @param ctx The manager context to let this User access other classes
	 */
    public Doctor(ManagerContext ctx) { super(ctx); }

    /**
     * Filled constructor for Doctor
     * @param ctx The manager context to let this User access other classes
     * @param name Name of the Doctor
     * @param isMale Gender of the Doctor, true if Male, false otherwise
     * @param contact Contact number of the Doctor
     * @param dob Day of birth of the Doctor
     */
    public Doctor(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        super(ctx, name, isMale, contact, dob);
        this.account = new Account(IdManager.generateId(Doctor.class));
    }

    public List<Timeslot> getBusyTimeslots() { return busyTimeslots; }

    public void addBusyTimeslot(Timeslot timeslot){ this.busyTimeslots.add(timeslot); }
    
    @Override
    public String toString() {
        return super.toString()+"\n User type: [Doctor]";
    }

    /**
     * {@inheritDoc}
     * Overrides the User.hydrate method to additionally hydrate the busyTimeslots attribute
     */
    @Override
    public void hydrate(HashMap<String, String> data){
        super.hydrate(data);

        // Hydrate busyTimeslots
        if (data.get("busyTimeslots").equals("")) return; // No busy timeslots

        String[] busyTimeslotsSerialized = data.get("busyTimeslots").split(DELIMITER);

        for (String timeslot : busyTimeslotsSerialized){
            this.busyTimeslots.add(new Timeslot(timeslot));
        }
    }

    /**
     * {@inheritDoc}
     * Overrides the User.serialize method to additionally serialize the busyTimeslots attribute
     */
    @Override
    public HashMap<String, String> serialize(){
        HashMap<String, String> data = super.serialize();

        // Serialize busyTimeslots
        StringBuilder busyTimeslotsSerialized = new StringBuilder();
        for (Timeslot timeslot : this.busyTimeslots){
            busyTimeslotsSerialized.append(timeslot.toString()).append(DELIMITER);
        }

        data.put("busyTimeslots", busyTimeslotsSerialized.toString());

        return data;
    }
}
