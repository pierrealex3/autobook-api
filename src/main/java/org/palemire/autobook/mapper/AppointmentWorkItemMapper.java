package org.palemire.autobook.mapper;

import org.mapstruct.*;
import org.hibernate.Hibernate;
import org.palemire.autobook.Response400Exception;
import org.palemire.autobook.dto.*;
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

@Mapper(componentModel = "spring", subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public abstract class AppointmentWorkItemMapper {

    @Autowired
    protected PieceRepository pieceRepository;

    @Named("appointmentWorkItemMapperDtoToEntity")
    abstract Set<AppointmentWorkItemEntity> fromDtoToEntity(List<AppointmentWorkItemDtoReq> dto);

    abstract AppointmentWorkItemLaborEntity fromDtoToEntity(AppointmentWorkItemLaborDto dto);

    PieceOperationEntity fromDtoToEntity(PieceOperationDtoReq dto) {
        if (dto == null)
            return null;
        if (dto.getPerformedAt() == null)
            throw new Response400Exception("Piece operation performedAt is required");

        PieceOperationEntity entity;
        if (dto instanceof PieceOperationBuyDtoReq)
            entity = new PieceOperationBuyEntity();
        else if (dto instanceof PieceOperationInstallDtoReq)
            entity = new PieceOperationInstallEntity();
        else if (dto instanceof PieceOperationRepairDtoReq)
            entity = new PieceOperationRepairEntity();
        else if (dto instanceof PieceOperationSellDtoReq)
            entity = new PieceOperationSellEntity();
        else if (dto instanceof PieceOperationThrashDtoReq)
            entity = new PieceOperationThrashEntity();
        else
            throw new IllegalStateException("Cannot map unmanaged subtype of PieceOperationDtoReq");

        entity.setCost(Optional.ofNullable(dto.getCost()).orElse(BigDecimal.ZERO));
        entity.setPerformedAt(dto.getPerformedAt());
        entity.setPiece(resolvePiece(dto.getPieceKindReq()));

        return entity;
    }

    @AfterMapping
    void linkBackreferences(@MappingTarget AppointmentWorkItemEntity appointmentWorkItem) {
        appointmentWorkItem.getAppointmentWorkItemsLabor()
                .forEach(labor -> labor.setAppointmentWorkItem(appointmentWorkItem));
    }

    PieceEntity resolvePiece(PieceKindDtoReq pieceKindDtoReq) {
        if (pieceKindDtoReq == null)
            return null;
        else if (pieceKindDtoReq instanceof PieceKindExistingDtoReq pieceKindDto)
            return verifyPieceIdExistsIfSpecified(pieceKindDto);
        else if (pieceKindDtoReq instanceof PieceKindNewDtoReq pieceKindDto) {
            return fromDtoToEntity(pieceKindDto.getPiece());
        }
        throw new IllegalStateException("Cannot map unmanaged subtype of PieceKindDtoReq");
    }

    @SubclassMapping(source = PieceBrakeDtoReq.class, target = PieceBrakeEntity.class)
    @SubclassMapping(source = PieceWiperDtoReq.class, target = PieceWiperEntity.class)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "xid", ignore = true)
    abstract PieceEntity fromDtoToEntity(PieceDtoReq pieceDto);

    PieceEntity verifyPieceIdExistsIfSpecified(PieceKindExistingDtoReq pieceInDto) {
        String pieceXid = Optional.ofNullable(pieceInDto).map(PieceKindExistingDtoReq::getPieceXid).orElse(null);
        if (pieceXid == null)
            return null;
        Optional<Long> pieceId = pieceRepository.findIdByXid(pieceXid);
        if (pieceId.isPresent())
            return pieceRepository.getReferenceById(pieceId.get());
        throw new Response400Exception("Piece ID specified: (%d) does not exist".formatted(pieceXid));
    }

    @Named("appointmentWorkItemMapperEntityToDto")
    abstract List<AppointmentWorkItemDtoRes> fromEntityToDto(Set<AppointmentWorkItemEntity> entity);

    PieceOperationDtoRes fromEntityToDto(PieceOperationEntity entity) {
        var piece = fromEntityToDto(entity.getPiece());
        var entityClass = Hibernate.getClass(entity);
        if (entityClass == PieceOperationBuyEntity.class)
            return new PieceOperationBuyDtoRes("buy", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationInstallEntity.class)
            return new PieceOperationInstallDtoRes("install", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationRepairEntity.class)
            return new PieceOperationRepairDtoRes("repair", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationSellEntity.class)
            return new PieceOperationSellDtoRes("sell", entity.getPerformedAt())
                    .cost(entity.getCost()).piece(piece);
        if (entityClass == PieceOperationThrashEntity.class)
            return new PieceOperationThrashDtoRes("thrash", entity.getPerformedAt())
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
