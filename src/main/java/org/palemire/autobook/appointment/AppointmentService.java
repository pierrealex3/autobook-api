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



    @Transactional
    public void createAppointment(Integer vehicleId, AppointmentEntity appointmentEntity) {
        var vehicleEntity = vehicleRepository.getReferenceById(vehicleId);
        appointmentEntity.setVehicle(vehicleEntity);
        appointmentRepository.save(appointmentEntity);
    }

    public AppointmentEntity getAppointment(Integer appointmentId) {
        var appointmentEntityOpt = appointmentRepository.findById(appointmentId);
        if (appointmentEntityOpt.isEmpty()) {
            throw new Response400Exception();
        }
        else
            return appointmentEntityOpt.get();
    }
}
