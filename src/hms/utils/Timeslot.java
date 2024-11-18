package hms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timeslot {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    private final Date startTime;
    private final Date endTime;
    private final long duration;

    /**
     * Constructs a Timeslot object with the specified start and end times.
     * @param startTime The start time of the timeslot.
     * @param endTime The end time of the timeslot.
     */
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

    /**
     * Gets the start time of the timeslot.
     * @return The start time of the timeslot.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the timeslot.
     * @return The end time of the timeslot.
     */
    public Date getEndTime() {
        return endTime;
    }

    /** 
     * Gets the duration of the timeslot in milliseconds.
     * @return The duration of the timeslot in milliseconds.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Gets the start time of the timeslot as a formatted string.
     * @return The start time of the timeslot as a formatted string.
     */
    public String getStartTimeString() {
        return sdf.format(startTime);
    }

    /**
     * Gets the end time of the timeslot as a formatted string.
     * @return The end time of the timeslot as a formatted string.
     */
    public String getEndTimeString() {
        return sdf.format(endTime);
    }

    /**
     * Checks if this timeslot overlaps with another timeslot.
     * @param other The other timeslot to check for overlap.
     * @return true if the timeslots overlap, false otherwise.
     */
    public boolean isOverlapping(Timeslot other) {
        return this.startTime.before(other.endTime) && this.endTime.after(other.startTime);
    }

    /**
     * Returns the union of two timeslots.
     * @param t1 The first timeslot.
     * @param t2 The second timeslot.
     * @return A new Timeslot representing the union of the two timeslots.
     */
    public static Timeslot union(Timeslot t1, Timeslot t2) {
        Date startTime = t1.startTime.before(t2.startTime) ? t1.startTime : t2.startTime;
        Date endTime = t1.endTime.after(t2.endTime) ? t1.endTime : t2.endTime;
        return new Timeslot(startTime, endTime);
    }

    /**
     * Returns the intersection of two timeslots.
     * @param t1 The first timeslot.
     * @param t2 The second timeslot.
     * @return A new Timeslot representing the intersection of the two timeslots.
     */
    public static Timeslot intersection(Timeslot t1, Timeslot t2) {
        Date startTime = t1.startTime.after(t2.startTime) ? t1.startTime : t2.startTime;
        Date endTime = t1.endTime.before(t2.endTime) ? t1.endTime : t2.endTime;
        return new Timeslot(startTime, endTime);
    }

    /**
     * Subtracts one timeslot from another.
     * @param t1 The timeslot to subtract from.
     * @param t2 The timeslot to subtract.
     * @return An array of Timeslot objects representing the result of the subtraction.
     */
    public static Timeslot[] subtract(Timeslot t1, Timeslot t2) {
        if (!t1.isOverlapping(t2)) {
            return new Timeslot[] { t1 };
        }

        if (t1.startTime.before(t2.startTime) && t1.endTime.after(t2.endTime)) {
            return new Timeslot[] {
                new Timeslot(t1.startTime, t2.startTime),
                new Timeslot(t2.endTime, t1.endTime)
            };
        }

        if (t1.startTime.before(t2.startTime)) { // t1.endTime <= t2.endTime
            return new Timeslot[] { new Timeslot(t1.startTime, t2.startTime) };
        }

        if (t1.endTime.after(t2.endTime)) { // t1.startTime >= t2.startTime
            return new Timeslot[] { new Timeslot(t2.endTime, t1.endTime) };
        }

        // t1.startTime >= t2.startTime && t1.endTime <= t2.endTime
        return new Timeslot[0];
    }

    /**
     * Returns a string representation of the timeslot.
     * @return A string representation of the timeslot.
     */
    @Override
    public String toString() {
        return sdf.format(startTime.getTime()) + " - " + sdf.format(endTime.getTime());
    }
}
