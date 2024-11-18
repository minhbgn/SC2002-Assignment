package hms.appointment.enums;

/** Enum representing the status of an appointment in the hospital management system. */
public enum AppointmentStatus {
    /** The pending appointment has been accepted by the doctor*/
    ACCEPTED,
    /** The appointment has been cancelled by the patient. */
    CANCELLED,
    /** The appointment has been rejected by the doctor. */
    REJECTED,
    /** The appointment has been finished. */
    FINISHED,
    /** The appointment is pending a response from the doctor. */
    PENDING
}