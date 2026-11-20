package org.palemire.autobook.mapper;

import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.SubclassMapping;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.palemire.autobook.Response400Exception;
import org.palemire.autobook.dto.PieceBrakeDtoReq;
import org.palemire.autobook.dto.PieceDtoReq;
import org.palemire.autobook.dto.PieceKindDtoReq;
import org.palemire.autobook.dto.PieceKindExistingDtoReq;
import org.palemire.autobook.dto.PieceKindNewDtoReq;
import org.palemire.autobook.dto.PieceSummaryDto;
import org.palemire.autobook.dto.PieceWiperDtoReq;
import org.palemire.autobook.entity.PieceBrakeEntity;
import org.palemire.autobook.entity.PieceEntity;
import org.palemire.autobook.entity.PieceTireEntity;
import org.palemire.autobook.entity.PieceWiperEntity;
import org.palemire.autobook.repository.PieceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring", subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public abstract class PieceMapper {

    @Autowired
    protected PieceRepository pieceRepository;

    @Named("resolvePiece")
    public PieceEntity resolvePiece(PieceKindDtoReq pieceKindDtoReq) {
        if (pieceKindDtoReq == null)
            return null;
        if (pieceKindDtoReq instanceof PieceKindExistingDtoReq pieceKindDto)
            return verifyPieceIdExistsIfSpecified(pieceKindDto);
        if (pieceKindDtoReq instanceof PieceKindNewDtoReq pieceKindDto)
            return fromDtoToEntity(pieceKindDto.getPiece());
        throw new IllegalStateException("Cannot map unmanaged subtype of PieceKindDtoReq");
    }

    @SubclassMapping(source = PieceBrakeDtoReq.class, target = PieceBrakeEntity.class)
    @SubclassMapping(source = PieceWiperDtoReq.class, target = PieceWiperEntity.class)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "xid", ignore = true)
    public abstract PieceEntity fromDtoToEntity(PieceDtoReq pieceDto);

    public PieceEntity verifyPieceIdExistsIfSpecified(PieceKindExistingDtoReq pieceInDto) {
        String pieceXid = Optional.ofNullable(pieceInDto)
                .map(PieceKindExistingDtoReq::getPieceXid)
                .orElse(null);
        if (pieceXid == null)
            return null;
        Optional<Long> pieceId = pieceRepository.findIdByXid(pieceXid);
        if (pieceId.isPresent())
            return pieceRepository.getReferenceById(pieceId.get());
        throw new Response400Exception("Piece ID specified: (%s) does not exist".formatted(pieceXid));
    }

    @Mapping(target = "category", source = "entity", qualifiedByName = "categoryDefinitionForPiece")
    public abstract PieceSummaryDto fromEntityToDto(PieceEntity entity);

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
