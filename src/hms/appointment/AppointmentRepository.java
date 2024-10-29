package hms.appointment;

import java.util.Date;

import hms.appointment.enums.AppointmentStatus;
import hms.common.AbstractRepository;
import hms.manager.ManagerContext;

public class AppointmentRepository extends AbstractRepository<Appointment> {
    public AppointmentRepository(ManagerContext ctx) {
        super(ctx);
        //TODO Auto-generated constructor stub
    }

    public Appointment create(String patientId, String doctorId, Date date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    public void updateDate(String id, Date date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDate'");
    }

    public void updateStatus(String id, AppointmentStatus status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStatus'");
    }

    @Override
    public Appointment get(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Appointment createEmptyModel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyModel'");
    }
}
