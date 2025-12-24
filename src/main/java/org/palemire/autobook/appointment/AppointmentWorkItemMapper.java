package org.palemire.autobook.appointment;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.palemire.autobook.Response400Exception;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AppointmentWorkItemMapper {

    @Autowired
    protected PieceRepository pieceRepository;

    @Named("appointmentWorkItemMapperDtoToEntity")
    abstract Set<AppointmentWorkItemEntity> fromDtoToEntity(List<AppointmentWorkItemDto> dto);

    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemLaborEntity fromDtoToEntity(AppointmentWorkItemLaborDto dto);

//    @SubclassMapping(target = AppointmentWorkItemPieceBuyEntity.class, source = AppointmentWorkItemPieceBuyDto.class)
//    @SubclassMapping(target = AppointmentWorkItemPieceInstallEntity.class, source = AppointmentWorkItemPieceInstallDto.class)
//    @SubclassMapping(target = AppointmentWorkItemPieceRemovalEntity.class, source = AppointmentWorkItemPieceRemovalDto.class)
//    @SubclassMapping(target = AppointmentWorkItemPieceRepairEntity.class, source = AppointmentWorkItemPieceRepairDto.class)
//    abstract AppointmentWorkItemPieceEntity fromDtoToEntity(AppointmentWorkItemPieceDto dto);

    // TODO this method cannot be the best solution possible... can't believe MapStruct cannot generate this kinda stuff.  FIND IT.
    AppointmentWorkItemPieceEntity fromDtoToEntity(AppointmentWorkItemPieceDto dto) {
        if (dto instanceof AppointmentWorkItemPieceBuyDto dtox)
            return fromDtoToEntity(dtox);
        else if (dto instanceof AppointmentWorkItemPieceInstallDto dtox)
            return fromDtoToEntity(dtox);
        else if (dto instanceof AppointmentWorkItemPieceRemovalDto dtox)
            return fromDtoToEntity(dtox);
        else if (dto instanceof AppointmentWorkItemPieceRepairDto dtox)
            return fromDtoToEntity(dtox);
        else throw new IllegalStateException("Cannot map unmanaged subtype of AppointmentWorkItemPieceDto");
    }

    @Mapping(target = "piece", source = "pieceId", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceBuyEntity fromDtoToEntity(AppointmentWorkItemPieceBuyDto dto);

    @Mapping(target = "piece", source = "pieceId", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceInstallEntity fromDtoToEntity(AppointmentWorkItemPieceInstallDto dto);

    @Mapping(target = "piece", source = "pieceId", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceRemovalEntity fromDtoToEntity(AppointmentWorkItemPieceRemovalDto dto);

    @Mapping(target = "piece", source = "pieceId", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceRepairEntity fromDtoToEntity(AppointmentWorkItemPieceRepairDto dto);

    @AfterMapping
    void linkBackreferences(@MappingTarget AppointmentWorkItemEntity appointmentWorkItem) {
        appointmentWorkItem.getAppointmentWorkItemsLabor().forEach(awil -> awil.setAppointmentWorkItem(appointmentWorkItem));
        appointmentWorkItem.getAppointmentWorkItemsPiece().forEach(awip -> awip.setAppointmentWorkItem(appointmentWorkItem));
    }

    @Named("verifyPieceIdExistsIfSpecified")
    PieceEntity verifyPieceIdExistsIfSpecified(Long pieceInDto) {
        if (pieceInDto == null)
            return null;
        if (pieceRepository.existsById(pieceInDto))
            return pieceRepository.getReferenceById(pieceInDto);
        else
            throw new Response400Exception("Piece ID specified: (%d) does not exist".formatted(pieceInDto));
    }

}
