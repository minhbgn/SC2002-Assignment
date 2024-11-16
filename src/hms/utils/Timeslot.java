package hms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timeslot {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    private final Date startTime;
    private final Date endTime;
    private final long duration;

    public Timeslot(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime.getTime() - startTime.getTime();
    }

    /** 
     * Constructs a Timeslot object from a String,
     * the String format must follow the same format
     * as the toString() method of this class.
     */
    public Timeslot(String timeslotString) {
        if (timeslotString == null) {
            throw new IllegalArgumentException("Timeslot string cannot be null");
        }

        String[] parts = timeslotString.split(" - ");
        
        try {
            this.startTime = new Date(sdf.parse(parts[0]).getTime());
            this.endTime = new Date(sdf.parse(parts[1]).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid timeslot string format");
        }

        this.duration = endTime.getTime() - startTime.getTime();
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    /** @return The duration of the timeslot in milliseconds */
    public long getDuration() {
        return duration;
    }

    public String getStartTimeString() {
        return sdf.format(startTime);
    }

    public String getEndTimeString() {
        return sdf.format(endTime);
    }

    public boolean isOverlapping(Timeslot other) {
        return this.startTime.before(other.endTime) && this.endTime.after(other.startTime);
    }

    public static Timeslot union(Timeslot t1, Timeslot t2) {
        Date startTime = t1.startTime.before(t2.startTime) ? t1.startTime : t2.startTime;
        Date endTime = t1.endTime.after(t2.endTime) ? t1.endTime : t2.endTime;
        return new Timeslot(startTime, endTime);
    }

    public static Timeslot intersection(Timeslot t1, Timeslot t2) {
        Date startTime = t1.startTime.after(t2.startTime) ? t1.startTime : t2.startTime;
        Date endTime = t1.endTime.before(t2.endTime) ? t1.endTime : t2.endTime;
        return new Timeslot(startTime, endTime);
    }

    @Override
    public String toString() {
        return sdf.format(startTime.getTime()) + " - " + sdf.format(endTime.getTime());
    }
}
