package hms.system;

import hms.appointment.Appointment;
import hms.appointment.enums.AppointmentStatus;
import hms.common.SearchCriterion;
import hms.manager.AppointmentManager;
import hms.manager.ManagerContext;
import hms.manager.PrescriptionManager;
import hms.manager.UserManager;
import hms.prescription.Prescription;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.repository.DoctorRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientSystem implements ISystem {
    /** The patient bound to this system */
    private final Patient patient;
    /** The active search criteria for appointments */
    private final List<SearchCriterion<Appointment, ?>> defaultCriteria;

    private final ManagerContext ctx;
    private final MenuNavigator menuNav;

    /** The next system to run. */
    private ISystem nextSystem = null;

    /**
     * Create a new patient system
     * @param ctx The manager context
     * @param patient The patient
     */
    public PatientSystem(ManagerContext ctx, Patient patient) {
        this.patient = patient;
        this.ctx = ctx;
        this.menuNav = new MenuNavigator();
    
        // Default search criteria for appointments
        this.defaultCriteria = List.of(
            new SearchCriterion<>(Appointment::getPatientId, patient.getAccount().getId())
        );

        this.nextSystem = this; // Default to the current system

        menuNav.addMenu(new SimpleMenu("Welcome to the Hospital Management System!", List.of(
            new UserOption("View Profile", this::handleViewProfile),
            new UserOption("View Appointments", this::handleViewAppointments),
            new UserOption("Schedule Appointment", this::handleScheduleAppointment),
            new UserOption("Log Out", () -> nextSystem = new LoginSystem(ctx)),
            new UserOption("Exit", () -> nextSystem = null)
        )));
    }
    
    private void handleViewProfile() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String profileInfo = String.format(
            "Patient Profile\n\nName: %s\nGender: %s\nContact: %s\nDate of Birth: %s\nAccount Status: %s",
            patient.name, patient.isMale ? "Male" : "Female", patient.contact, sdf.format(patient.dob),
            patient.getAccount().isActive() ? "Active" : "Inactive"
        );

        UserOption updateContactOption = new UserOption("Update Contact", () -> {
            String newContact = Prompt.getStringInput("Enter your new contact info: ");
            patient.contact = newContact;
        });

        UserOption updateDobOption = new UserOption("Update Date of Birth", () -> {
            Date newDob = Prompt.getDateInput("Enter your new date of birth: ");
            patient.dob = newDob;
        });

        UserOption changePasswordOption = new UserOption("Change password", () -> {
            String oldPassword = Prompt.getStringInput("Enter your old password: ");
            if(!patient.getAccount().authenticate(oldPassword)){
                System.out.println("Wrong password");
                return;
            }

            String newPassword = Prompt.getStringInput("Enter your new password: ");
            String newPasswordConfirm = Prompt.getStringInput("Confirm your new password: ");
        
            if(!newPassword.equals(newPasswordConfirm)){
                System.out.println("Passwords do not match");
                return;
            }
            
            patient.getAccount().setPassword(newPassword);
            System.out.println("Password changed successfully");
        });

        SimpleMenu profileMenu = new SimpleMenu(profileInfo, List.of(
            updateContactOption, updateDobOption, changePasswordOption
        ));

        menuNav.addMenu(profileMenu);
    }

    private void handleViewAppointments() {
        List<Appointment> appointments = ctx
            .getManager(AppointmentManager.class)
            .getAppointments(defaultCriteria);

        if (appointments.isEmpty()) {
            System.out.println("You have not made any appointments");
            return;
        }
        
        PaginatedListSelector<Appointment> viewer = new PaginatedListSelector<>(
            "Appointments", appointments.toArray(Appointment[]::new),
            (Appointment appointment) -> {
                String appointmentInfo = String.format(
                    "Appointment with %s on %s\nStatus: %s",
                    appointment.getDoctorId(), appointment.getDate(),
                    appointment.getStatus().toString()
                );
                
                SimpleMenu appointmentMenu = new SimpleMenu(appointmentInfo, null);

                if(appointment.getStatus() == AppointmentStatus.PENDING
                    || appointment.getStatus() == AppointmentStatus.ACCEPTED) {
                    UserOption cancelOption = new UserOption(
                        "Cancel Appointment",
                        () -> patient.cancelAppointment(appointment.getId())
                    );
    
                    UserOption rescheduleOption = new UserOption(
                        "Reschedule Appointment", () -> {
                            Date newDate = Prompt.getDateInput("Enter the new date for your appointment: ");
                            patient.rescheduleAppointment(appointment.getId(), newDate);
                        }
                    );
        
                    appointmentMenu.addOption(cancelOption);
                    appointmentMenu.addOption(rescheduleOption);
                }

                if(appointment.getStatus() == AppointmentStatus.FINISHED) {
                    UserOption viewRecords = new UserOption(
                        "View Records",
                        () -> {
                            String recordInfo = String.format(
                                "Notes: %s\nServices: %s",
                                appointment.getRecord().getNotes(),
                                appointment.getRecord().getService()
                            );

                            ArrayList<String> prescriptions = appointment.getRecord().getPrescriptions();

                            if(!prescriptions.isEmpty()) {
                                recordInfo += "\nPrescriptions: ";
                                PrescriptionManager prescriptionManager = ctx.getManager(PrescriptionManager.class);

                                for(String prescription : prescriptions) {
                                    Prescription p = prescriptionManager.getPrescriptions(List.of(
                                        new SearchCriterion<>(Prescription::getId, prescription)
                                    )).get(0);

                                    if(p != null) {
                                        recordInfo += "\n" + p;
                                    }
                                }
                            }

                            SimpleMenu recordMenu = new SimpleMenu(recordInfo, null);

                            menuNav.addMenu(recordMenu);
                        }
                    );

                    appointmentMenu.addOption(viewRecords);
                }
            
                menuNav.addMenu(appointmentMenu);
            }
        );
        
        menuNav.addMenu(viewer);
    }

    private void handleScheduleAppointment() {
        System.out.println("Scheduling an appointment");

        Doctor[] doctors = ctx.getManager(UserManager.class)
            .getRepository(DoctorRepository.class)
            .findWithFilters(null)
            .toArray(Doctor[]::new);

        PaginatedListSelector<Doctor> doctorSelector = new PaginatedListSelector<>(
            "Select a doctor for your appointment", doctors,
            (Doctor doctor) -> {
                // Clear the screen
                System.out.println("\033[H\033[2J");

                System.out.println("Selected doctor: " + doctor);

                String doctorId = doctor.getAccount().getId();
                Date date = Prompt.getDateInput("Enter the date for your appointment: ");
    
                Appointment appointment = ctx
                    .getManager(AppointmentManager.class)
                    .makeAppointment(patient.getAccount().getId(), doctorId, date);
    
                if(appointment == null) {
                    System.out.println("Failed to schedule appointment");
                    return;
                }
    
                System.out.println("Appointment scheduled: " + appointment);

                menuNav.popMenu();
            }
        );

        menuNav.addMenu(doctorSelector);
    }

    @Override
    public ISystem run() {
        menuNav.display(true);

        String choice = Prompt.getStringInput("Enter your choice: ");

        if (!menuNav.hasOption(choice)){
            System.out.println("Invalid choice");
            return this;
        }

        menuNav.executeOption(choice);

        return nextSystem;
    }
}
