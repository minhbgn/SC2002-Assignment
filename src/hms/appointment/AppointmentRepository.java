package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.AbstractRepository;
import hms.manager.ManagerContext;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentRepository extends AbstractRepository<Appointment> {
    public AppointmentRepository(ManagerContext ctx) {
        super(ctx);
    }

    public Appointment create(String patientId, String doctorId, Date date) {
        Appointment appointment = new Appointment(patientId, doctorId, date, AppointmentStatus.PENDING);
        models.add(appointment);
        return appointment;
    }

    public void updateDate(String id, Date date) {
        Appointment appointment = get(id);
            appointment.setDate(date);
    }

    public void updateStatus(String id, AppointmentStatus status) {
        Appointment appointment = get(id);
            appointment.setStatus(status);
    }

    public void updateRecord(String id, String service, ArrayList<String> prescriptionIds, String notes) {
        get(id).setRecord(service, prescriptionIds, notes);
    }

    @Override
    public Appointment get(String id) {
        return models.stream()
        .filter(appointment -> appointment.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + id));
    }

    @Override
    public Appointment createEmptyModel() {
        return new Appointment();
    }
}
