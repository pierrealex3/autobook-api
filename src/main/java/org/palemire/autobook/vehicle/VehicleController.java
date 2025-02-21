package org.palemire.autobook.vehicle;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.Constants;
import org.palemire.autobook.appointment.AppointmentDto;
import org.palemire.autobook.appointment.AppointmentMapper;
import org.palemire.autobook.appointment.AppointmentService;
import org.palemire.autobook.event.EventDto;
import org.palemire.autobook.event.FetchEventsService;
import org.palemire.autobook.vehicle.fetch.FetchVehicleService;
import org.palemire.autobook.vehicle.fetch.VehicleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constants.AUTOBOOK_API_ROOT)
@RequiredArgsConstructor
public class VehicleController {

    private final FetchVehicleService fetchVehicleService;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentService appointmentService;
    private final FetchEventsService fetchEventsService;

    @GetMapping( path = "/vehicles")
    public ResponseEntity<List<VehicleDto>> getAllVehicles(@RequestHeader("X-MockUser") String userid) {
        List<VehicleDto> vehicleDtoList = fetchVehicleService.getAllVehicles(userid);
        return new ResponseEntity<>(vehicleDtoList, HttpStatus.OK);
    }

    @PostMapping(path = "/vehicles/{vehicleId}/appointment", consumes = "application/json")
    public ResponseEntity<Void> createAppointment(@PathVariable("vehicleId") Integer vehicleId, @RequestBody AppointmentDto appointmentDto) {
        var appointmentEntity = appointmentMapper.fromDtoToEntity(appointmentDto);
        appointmentService.createAppointment(vehicleId, appointmentEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable("appointmentId") Integer appointmentId) {
        var appointmentEntity = appointmentService.getAppointment(appointmentId);
        var appointmentDto = appointmentMapper.fromEntityToDto(appointmentEntity);
        return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
    }

    @PutMapping(path = "/appointments/{appointmentId}")
    public ResponseEntity<Void> modifyAppointment(@PathVariable("appointmentId") Integer appointmentId, @RequestBody AppointmentDto appointmentDto) {
        var detachedEntity = appointmentMapper.fromDtoToEntity(appointmentDto);
        appointmentService.modifyAppointment(appointmentId, detachedEntity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/events")
    public List<EventDto> getAllEvents(@RequestHeader("X-MockUser") String userid) {
        return fetchEventsService.getAllEvents(userid);
    }



}
