package hms.user.model;

import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Doctor extends User {
    private static String DELIMITER = ";";

    private final List<Timeslot> busyTimeslots = new ArrayList<>();

	/**
	 * Empty constructor to be used for hydration
	 * @param ctx The manager context to let this User access other classes
	 */
    public Doctor(ManagerContext ctx) {
        super(ctx);
    }

    public List<Timeslot> getBusyTimeslots() {
        return busyTimeslots;
    }

    public void addBusyTimeslot(Timeslot timeslot){
        this.busyTimeslots.add(timeslot);
    }

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
    
    /** To aid in printing out a Doctor object */
    @Override
    public String toString() {
        return super.toString()+"\n User type: [Doctor]";
    }

    @Override
    public void hydrate(HashMap<String, String> data){
        super.hydrate(data);

        // Hydrate busyTimeslots
        if (data.get("busyTimeslots").equals("")) return; // No busy timeslots

        System.out.println("busyTimeslots: " + data.get("busyTimeslots"));

        String[] busyTimeslotsSerialized = data.get("busyTimeslots").split(DELIMITER);
        System.out.println("busyTimeslotsSerialized: " + Arrays.toString(busyTimeslotsSerialized));

        for (String timeslot : busyTimeslotsSerialized){
            System.out.println("Adding timeslot: " + timeslot);
            this.busyTimeslots.add(new Timeslot(timeslot));
        }
    }

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
