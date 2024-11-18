package hms.user.model;

import hms.common.id.IdManager;
import hms.manager.ManagerContext;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class describes an account of a Doctor. 
 * The system will recognize accounts used by this class as doctors
 * DoctorService, the option of services for doctors, will be given to these users by the account
 */
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

    /**
     * Get the busy time slots of a Doctor. 
     * A patient will not be able to make an appointment on these busy time slots
     * @return the list of time slots in which Doctor is unavailable
     */
    public List<Timeslot> getBusyTimeslots() {
        return busyTimeslots;
    }

    /**
     * Add a new busy time slot to the list of busy time slot.
     * A patient will not be able to make an appointment on these busy time slots
     * @param timeslot The time slot in which the doctor is unavailable
     */
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

    /**
     * Hydrate the Doctor object with data from a HashMap.
     * @param data The data to hydrate the Doctor object with.
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
     * Serialize the Doctor object into a HashMap.
     * @return The serialized data of the Doctor object.
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
