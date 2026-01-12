package org.palemire.autobook.appointment;


import lombok.RequiredArgsConstructor;
import org.palemire.autobook.Response400Exception;
import org.palemire.autobook.vehicle.cud.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final VehicleRepository vehicleRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public void createAppointment(Integer vehicleId, AppointmentEntity appointmentEntity) {
        var vehicleEntity = vehicleRepository.getReferenceById(vehicleId);
        appointmentEntity.setVehicle(vehicleEntity);
        appointmentRepository.save(appointmentEntity);
    }

    @Transactional
    public void modifyAppointment(String appointmentId, AppointmentEntity detachedEntity) {
        var appointmentEntity = appointmentRepository.findByXid(appointmentId).orElseThrow(Response400Exception::new);
        appointmentMapper.fromDetachedToManaged(detachedEntity, appointmentEntity);
    }

    public AppointmentEntity getAppointment(String appointmentId) {
        return appointmentRepository.findByXid(appointmentId).orElseThrow(Response400Exception::new);
    }
}
