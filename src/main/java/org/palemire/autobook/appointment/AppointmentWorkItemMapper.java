package org.palemire.autobook.appointment;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.palemire.autobook.Response400Exception;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AppointmentWorkItemMapper {

    @Autowired
    protected PieceRepository pieceRepository;

    /**
     * MAPPINGS: dto to entity
     */

    @Named("appointmentWorkItemMapperDtoToEntity")
    abstract Set<AppointmentWorkItemEntity> fromDtoToEntity(List<AppointmentWorkItemDto> dto);

    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemLaborEntity fromDtoToEntity(AppointmentWorkItemLaborDto dto);

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

    @Mapping(target = "piece", source = "piece", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceBuyEntity fromDtoToEntity(AppointmentWorkItemPieceBuyDto dto);

    @Mapping(target = "piece", source = "piece", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceInstallEntity fromDtoToEntity(AppointmentWorkItemPieceInstallDto dto);

    @Mapping(target = "piece", source = "piece", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceRemovalEntity fromDtoToEntity(AppointmentWorkItemPieceRemovalDto dto);

    @Mapping(target = "piece", source = "piece", qualifiedByName = "verifyPieceIdExistsIfSpecified")
    @Mapping(target = "appointmentWorkItem", ignore = true)
    abstract AppointmentWorkItemPieceRepairEntity fromDtoToEntity(AppointmentWorkItemPieceRepairDto dto);

    @AfterMapping
    void linkBackreferences(@MappingTarget AppointmentWorkItemEntity appointmentWorkItem) {
        appointmentWorkItem.getAppointmentWorkItemsLabor().forEach(awil -> awil.setAppointmentWorkItem(appointmentWorkItem));
        appointmentWorkItem.getAppointmentWorkItemsPiece().forEach(awip -> awip.setAppointmentWorkItem(appointmentWorkItem));
    }

    @Named("verifyPieceIdExistsIfSpecified")
    PieceEntity verifyPieceIdExistsIfSpecified(PieceSummaryDto pieceInDto) {
        Long pieceIdInDto = Optional.ofNullable(pieceInDto).map(PieceSummaryDto::getId).orElse(null);

        if (pieceIdInDto == null)
            return null;
        if (pieceRepository.existsById(pieceIdInDto))
            return pieceRepository.getReferenceById(pieceIdInDto);
        else
            throw new Response400Exception("Piece ID specified: (%d) does not exist".formatted(pieceIdInDto));
    }

    /**
     * MAPPINGS: entity to dto
     */

    @Named("appointmentWorkItemMapperEntityToDto")
    abstract List<AppointmentWorkItemDto> fromEntityToDto(Set<AppointmentWorkItemEntity> entity);

    AppointmentWorkItemPieceDto fromEntityToDto(AppointmentWorkItemPieceEntity entity) {
        if (entity instanceof AppointmentWorkItemPieceBuyEntity entityx)
            return fromEntityToDto(entityx);
        else if (entity instanceof AppointmentWorkItemPieceInstallEntity entityx)
            return fromEntityToDto(entityx);
        else if (entity instanceof AppointmentWorkItemPieceRemovalEntity entityx)
            return fromEntityToDto(entityx);
        else if (entity instanceof AppointmentWorkItemPieceRepairEntity entityx)
            return fromEntityToDto(entityx);
        else throw new IllegalStateException("Cannot map unmanaged subtype of AppointmentWorkItemPieceEntity");
    }

    abstract AppointmentWorkItemPieceBuyDto fromEntityToDto(AppointmentWorkItemPieceBuyEntity entity);

    abstract AppointmentWorkItemPieceInstallDto fromEntityToDto(AppointmentWorkItemPieceInstallEntity entity);

    abstract AppointmentWorkItemPieceRemovalDto fromEntityToDto(AppointmentWorkItemPieceRemovalEntity entity);

    abstract AppointmentWorkItemPieceRepairDto fromEntityToDto(AppointmentWorkItemPieceRepairEntity entity);

    @Mapping(target = "category", source = "entity", qualifiedByName = "categoryDefinitionForPiece", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    abstract PieceSummaryDto fromEntityToDto(PieceEntity entity);

    @Named("categoryDefinitionForPiece")
    String categoryDefinitionForPiece(PieceEntity piece) {
        if (piece == null)
            return null;
        if (piece instanceof PieceTireEntity)
            return "TIRE";
        throw new IllegalStateException("Cannot map unmanaged subtype of PieceEntity");
    }


}
