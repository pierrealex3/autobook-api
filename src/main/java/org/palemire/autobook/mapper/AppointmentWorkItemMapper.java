package org.palemire.autobook.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.hibernate.Hibernate;
import org.palemire.autobook.Response400Exception;
import org.palemire.autobook.dto.AppointmentWorkItemDto;
import org.palemire.autobook.dto.AppointmentWorkItemLaborDto;
import org.palemire.autobook.dto.PieceOperationBuyDto;
import org.palemire.autobook.dto.PieceOperationDto;
import org.palemire.autobook.dto.PieceOperationInstallDto;
import org.palemire.autobook.dto.PieceOperationRepairDto;
import org.palemire.autobook.dto.PieceOperationSellDto;
import org.palemire.autobook.dto.PieceOperationThrashDto;
import org.palemire.autobook.dto.PieceSummaryDto;
import org.palemire.autobook.entity.AppointmentWorkItemEntity;
import org.palemire.autobook.entity.AppointmentWorkItemLaborEntity;
import org.palemire.autobook.entity.PieceBrakeEntity;
import org.palemire.autobook.entity.PieceEntity;
import org.palemire.autobook.entity.PieceOperationBuyEntity;
import org.palemire.autobook.entity.PieceOperationEntity;
import org.palemire.autobook.entity.PieceOperationInstallEntity;
import org.palemire.autobook.entity.PieceOperationRepairEntity;
import org.palemire.autobook.entity.PieceOperationSellEntity;
import org.palemire.autobook.entity.PieceOperationThrashEntity;
import org.palemire.autobook.entity.PieceTireEntity;
import org.palemire.autobook.entity.PieceWiperEntity;
import org.palemire.autobook.repository.PieceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AppointmentWorkItemMapper {

    @Autowired
    protected PieceRepository pieceRepository;

    @Named("appointmentWorkItemMapperDtoToEntity")
    abstract Set<AppointmentWorkItemEntity> fromDtoToEntity(List<AppointmentWorkItemDto> dto);

    abstract AppointmentWorkItemLaborEntity fromDtoToEntity(AppointmentWorkItemLaborDto dto);

    PieceOperationEntity fromDtoToEntity(PieceOperationDto dto) {
        if (dto == null)
            return null;
        if (dto.getPerformedAt() == null)
            throw new Response400Exception("Piece operation performedAt is required");

        PieceOperationEntity entity;
        if (dto instanceof PieceOperationBuyDto)
            entity = new PieceOperationBuyEntity();
        else if (dto instanceof PieceOperationInstallDto)
            entity = new PieceOperationInstallEntity();
        else if (dto instanceof PieceOperationRepairDto)
            entity = new PieceOperationRepairEntity();
        else if (dto instanceof PieceOperationSellDto)
            entity = new PieceOperationSellEntity();
        else if (dto instanceof PieceOperationThrashDto)
            entity = new PieceOperationThrashEntity();
        else
            throw new IllegalStateException("Cannot map unmanaged subtype of PieceOperationDto");

        entity.setCost(Optional.ofNullable(dto.getCost()).orElse(BigDecimal.ZERO));
        entity.setPerformedAt(dto.getPerformedAt());
        entity.setPiece(verifyPieceIdExistsIfSpecified(dto.getPiece()));

        return entity;
    }

    @AfterMapping
    void linkBackreferences(@MappingTarget AppointmentWorkItemEntity appointmentWorkItem) {
        appointmentWorkItem.getAppointmentWorkItemsLabor()
                .forEach(labor -> labor.setAppointmentWorkItem(appointmentWorkItem));
    }

    PieceEntity verifyPieceIdExistsIfSpecified(PieceSummaryDto pieceInDto) {
        Long pieceId = Optional.ofNullable(pieceInDto).map(PieceSummaryDto::getId).orElse(null);
        if (pieceId == null)
            return null;
        if (pieceRepository.existsById(pieceId))
            return pieceRepository.getReferenceById(pieceId);
        throw new Response400Exception("Piece ID specified: (%d) does not exist".formatted(pieceId));
    }

    @Named("appointmentWorkItemMapperEntityToDto")
    abstract List<AppointmentWorkItemDto> fromEntityToDto(Set<AppointmentWorkItemEntity> entity);

    PieceOperationDto fromEntityToDto(PieceOperationEntity entity) {
        var piece = fromEntityToDto(entity.getPiece());
        var entityClass = Hibernate.getClass(entity);
        if (entityClass == PieceOperationBuyEntity.class)
            return new PieceOperationBuyDto("buy", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationInstallEntity.class)
            return new PieceOperationInstallDto("install", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationRepairEntity.class)
            return new PieceOperationRepairDto("repair", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationSellEntity.class)
            return new PieceOperationSellDto("sell", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationThrashEntity.class)
            return new PieceOperationThrashDto("thrash", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        throw new IllegalStateException("Cannot map unmanaged subtype of PieceOperationEntity");
    }

    @Mapping(target = "category", source = "entity", qualifiedByName = "categoryDefinitionForPiece")
    abstract PieceSummaryDto fromEntityToDto(PieceEntity entity);

    @Named("categoryDefinitionForPiece")
    String categoryDefinitionForPiece(PieceEntity piece) {
        if (piece == null)
            return null;
        var pieceType = Hibernate.getClass(piece);
        if (pieceType == PieceTireEntity.class)
            return "TIRE";
        if (pieceType == PieceWiperEntity.class)
            return "WIPER";
        if (pieceType == PieceBrakeEntity.class)
            return "BRAKE";
        throw new IllegalStateException("Cannot map unmanaged subtype of PieceEntity");
    }
}
