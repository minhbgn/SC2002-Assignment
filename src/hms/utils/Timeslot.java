package hms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Represents a timeslot with a start time and an end time. */
public class Timeslot {
    /**
     * The date format used for parsing and formatting timeslot strings
     * with the format is "dd/MM/yyyy HH:mm:ss".
     * <p>
     * This format will be used for the {@link #toString()},
     * {@link #getStartTimeString()} and {@link #getEndTimeString()} methods.
     * @see #toString()
     * @see #getStartTimeString()
     * @see #getEndTimeString()
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    /** The start time of the timeslot. */
    private final Date startTime;
    /** The end time of the timeslot. */
    private final Date endTime;
    /** The duration of the timeslot in milliseconds. */
    private final long duration;

    /** 
     * Constructs a Timeslot object with the given start and end times.
     * @param startTime The start time of the timeslot
     * @param endTime The end time of the timeslot
     */
    public Timeslot(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime.getTime() - startTime.getTime();
    }

    /** 
     * Constructs a Timeslot object from a String,
     * the String format must follow the same format
     * as the {@link #toString()} method of this class.
     * <p>
     * This acts as a parsing method for the timeslot as well.
     * @param timeslotString A string representation of the timeslot
     * @throws IllegalArgumentException if the timeslot string is null or has an invalid format
     * @see #toString()
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

    public Date getStartTime() { return startTime; }
    public Date getEndTime() { return endTime; }
    public long getDuration() { return duration; }
    public String getStartTimeString() { return sdf.format(startTime); }
    public String getEndTimeString() { return sdf.format(endTime); }

    /** 
     * Checks if this timeslot overlaps with another timeslot.
     * @param other The other timeslot to check for overlap
     * @return true if the timeslots overlap, false otherwise
     */
    public boolean isOverlapping(Timeslot other) {
        return this.startTime.before(other.endTime) && this.endTime.after(other.startTime);
    }

    /** 
     * Performs a union operation on two timeslots.
     * @param t1 The first timeslot
     * @param t2 The second timeslot
     * @return A new timeslot representing the union of the two timeslots
     */
    public static Timeslot union(Timeslot t1, Timeslot t2) {
        Date startTime = t1.startTime.before(t2.startTime) ? t1.startTime : t2.startTime;
        Date endTime = t1.endTime.after(t2.endTime) ? t1.endTime : t2.endTime;
        return new Timeslot(startTime, endTime);
    }

    /** 
     * Performs an intersection operation on two timeslots.
     * @param t1 The first timeslot
     * @param t2 The second timeslot
     * @return A new timeslot representing the intersection of the two timeslots
     */
    public static Timeslot intersection(Timeslot t1, Timeslot t2) {
        Date startTime = t1.startTime.after(t2.startTime) ? t1.startTime : t2.startTime;
        Date endTime = t1.endTime.before(t2.endTime) ? t1.endTime : t2.endTime;
        return new Timeslot(startTime, endTime);
    }

    /** 
     * Performs a subtraction operation on two timeslots.
     * @param t1 The first timeslot
     * @param t2 The second timeslot
     * @return An array of timeslots representing the results of
     * subtracting the second timeslot from the first timeslot (t1 - t2)
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
     * The format is "dd/MM/yyyy HH:mm:ss - dd/MM/yyyy HH:mm:ss".
     * <p>
     * This acts as a serialization method for the timeslot too.
     * @return A string representation of the timeslot
     */
    @Override
    public String toString() {
        return sdf.format(startTime.getTime()) + " - " + sdf.format(endTime.getTime());
    }
}
