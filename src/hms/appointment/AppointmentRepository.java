package hms.appointment;

import hms.appointment.enums.AppointmentStatus;
import hms.common.AbstractRepository;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.utils.Timeslot;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository extends AbstractRepository<Appointment> {
    public AppointmentRepository(ManagerContext ctx) { super(ctx); }

    public Appointment create(String patientId, String doctorId, Timeslot timeslot) {
        Appointment appointment = new Appointment(patientId, doctorId, timeslot, AppointmentStatus.PENDING);
        models.add(appointment);
        return appointment;
    }

    public void updateTimeslot(String id, Timeslot timeslot) { get(id).setTimeslot(timeslot); }

    public void updateStatus(String id, AppointmentStatus status) { get(id).setStatus(status); }

    public void updateRecord(String id, String service, ArrayList<String> prescriptionIds, String notes) {
        get(id).setRecord(service, prescriptionIds, notes);
    }

    @Override
    public Appointment get(String id) {
        return findWithFilters(List.of(new SearchCriterion<>(Appointment::getId, id))).get(0);
    }

    @Override
    public Appointment createEmptyModel() {
        return new Appointment();
    }
}
