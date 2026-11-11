package org.palemire.autobook.controller;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.api.VehicleApi;
import org.palemire.autobook.dto.AppointmentCreatedDto;
import org.palemire.autobook.dto.AppointmentDto;
import org.palemire.autobook.dto.EventDto;
import org.palemire.autobook.dto.VehicleDto;
import org.palemire.autobook.mapper.AppointmentMapper;
import org.palemire.autobook.service.AppointmentService;
import org.palemire.autobook.service.FetchEventsService;
import org.palemire.autobook.service.FetchVehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VehicleController implements VehicleApi {

    private final FetchVehicleService fetchVehicleService;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentService appointmentService;
    private final FetchEventsService fetchEventsService;

    public ResponseEntity<List<VehicleDto>> getAllVehicles(String xMockUser) {
        return ResponseEntity.ok(fetchVehicleService.getAllVehicles(xMockUser));
    }

    public ResponseEntity<AppointmentCreatedDto> createAppointment(Integer vehicleId, AppointmentDto appointmentDto) {
        var appointmentEntity = appointmentMapper.fromDtoToEntity(appointmentDto);
        appointmentService.createAppointment(vehicleId, appointmentEntity);
        var appointmentCreatedDto = new AppointmentCreatedDto().xid(appointmentEntity.getXid());
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentCreatedDto);
    }

    public ResponseEntity<AppointmentDto> getAppointment(String appointmentId) {
        var appointmentEntity = appointmentService.getAppointment(appointmentId);
        var appointmentDto = appointmentMapper.fromEntityToDto(appointmentEntity);
        return ResponseEntity.ok(appointmentDto);
    }

    public ResponseEntity<Void> modifyAppointment(String appointmentId, AppointmentDto appointmentDto) {
        var detachedEntity = appointmentMapper.fromDtoToEntity(appointmentDto);
        appointmentService.modifyAppointment(appointmentId, detachedEntity);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<EventDto>> getAllEvents(String xMockUser) {
        return ResponseEntity.ok(fetchEventsService.getAllEvents(xMockUser));
    }
}
