package org.palemire.autobook.mapper;

import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;
import org.palemire.autobook.Response400Exception;
import org.palemire.autobook.dto.PieceOperationBuyDtoReq;
import org.palemire.autobook.dto.PieceOperationBuyDtoRes;
import org.palemire.autobook.dto.PieceOperationDtoReq;
import org.palemire.autobook.dto.PieceOperationDtoRes;
import org.palemire.autobook.dto.PieceOperationInstallDtoReq;
import org.palemire.autobook.dto.PieceOperationInstallDtoRes;
import org.palemire.autobook.dto.PieceOperationRepairDtoReq;
import org.palemire.autobook.dto.PieceOperationRepairDtoRes;
import org.palemire.autobook.dto.PieceOperationSellDtoReq;
import org.palemire.autobook.dto.PieceOperationSellDtoRes;
import org.palemire.autobook.dto.PieceOperationThrashDtoReq;
import org.palemire.autobook.dto.PieceOperationThrashDtoRes;
import org.palemire.autobook.entity.PieceOperationBuyEntity;
import org.palemire.autobook.entity.PieceOperationEntity;
import org.palemire.autobook.entity.PieceOperationInstallEntity;
import org.palemire.autobook.entity.PieceOperationRepairEntity;
import org.palemire.autobook.entity.PieceOperationSellEntity;
import org.palemire.autobook.entity.PieceOperationThrashEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = PieceMapper.class,
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public abstract class PieceOperationMapper {

    @Autowired
    protected PieceMapper pieceMapper;

    @SubclassMapping(source = PieceOperationBuyDtoReq.class, target = PieceOperationBuyEntity.class)
    @SubclassMapping(source = PieceOperationInstallDtoReq.class, target = PieceOperationInstallEntity.class)
    @SubclassMapping(source = PieceOperationRepairDtoReq.class, target = PieceOperationRepairEntity.class)
    @SubclassMapping(source = PieceOperationSellDtoReq.class, target = PieceOperationSellEntity.class)
    @SubclassMapping(source = PieceOperationThrashDtoReq.class, target = PieceOperationThrashEntity.class)
    @Mapping(target = "piece", source = "dto.pieceKindReq", qualifiedByName = "resolvePiece")
    public abstract PieceOperationEntity fromDtoToEntity(PieceOperationDtoReq dto);

    public PieceOperationDtoRes fromEntityToDto(PieceOperationEntity entity) {
        var piece = pieceMapper.fromEntityToDto(entity.getPiece());
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
}
