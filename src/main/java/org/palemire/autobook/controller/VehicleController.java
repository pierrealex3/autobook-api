package org.palemire.autobook.controller;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.api.VehicleApi;
import org.palemire.autobook.dto.*;
import org.palemire.autobook.mapper.AppointmentMapper;
import org.palemire.autobook.service.AppointmentService;
import org.palemire.autobook.service.FetchEventsService;
import org.palemire.autobook.service.FetchVehicleService;
import org.palemire.autobook.service.PieceService;
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
    private final PieceService pieceService;

    public ResponseEntity<List<VehicleDto>> getAllVehicles(String xMockUser) {
        return ResponseEntity.ok(fetchVehicleService.getAllVehicles(xMockUser));
    }

    public ResponseEntity<AppointmentCreatedDto> createAppointment(Integer vehicleId, AppointmentDtoReq appointmentDto) {
        List<String> pieceXidsInDto = extractPieceXidsFromAppointmentDto(appointmentDto);
        if (pieceService.piecesNotAssociatedToVehicle(pieceXidsInDto, vehicleId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var appointmentEntity = appointmentMapper.fromDtoToEntity(appointmentDto);
        appointmentService.createAppointment(vehicleId, appointmentEntity);
        var appointmentCreatedDto = new AppointmentCreatedDto().xid(appointmentEntity.getXid());
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentCreatedDto);
    }

    public ResponseEntity<AppointmentDtoRes> getAppointment(String appointmentId) {
        var appointmentEntity = appointmentService.getAppointment(appointmentId);
        var appointmentDto = appointmentMapper.fromEntityToDto(appointmentEntity);
        return ResponseEntity.ok(appointmentDto);
    }

    @Override
    public ResponseEntity<List<PieceSummaryDto>> getVehiclePieces(Integer vehicleId) {
        return ResponseEntity.ok(pieceService.getPiecesSummariesForVehicle(vehicleId));
    }

    public ResponseEntity<Void> modifyAppointment(String appointmentId, AppointmentDtoReq appointmentDto) {
        List<String> pieceXidsInDto = extractPieceXidsFromAppointmentDto(appointmentDto);
        Integer vehicleId = appointmentService.getVehicleIdForAppointment(appointmentId);
        if (pieceService.piecesNotAssociatedToVehicle(pieceXidsInDto, vehicleId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var detachedEntity = appointmentMapper.fromDtoToEntity(appointmentDto);
        appointmentService.modifyAppointment(appointmentId, detachedEntity);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<EventDto>> getAllEvents(String xMockUser) {
        return ResponseEntity.ok(fetchEventsService.getAllEvents(xMockUser));
    }

    List<String> extractPieceXidsFromAppointmentDto(AppointmentDtoReq appointmentDto) {
        return appointmentDto.getAppointmentWorkItems().stream()
                .flatMap(appointmentWorkItemDto -> appointmentWorkItemDto.getPieceOperations().stream())
                .map(PieceOperationDtoReq::getPieceKindReq)
                .filter(PieceKindExistingDtoReq.class::isInstance)
                .map(PieceKindExistingDtoReq.class::cast)
                .map(PieceKindExistingDtoReq::getPieceXid)
                .toList();
    }
}
