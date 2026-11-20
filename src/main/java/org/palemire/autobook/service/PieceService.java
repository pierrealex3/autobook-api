package org.palemire.autobook.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.palemire.autobook.dto.PieceSummaryDto;
import org.palemire.autobook.entity.PieceEntity;
import org.palemire.autobook.mapper.PieceMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PieceService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final PieceMapper pieceMapper;

    public List<PieceSummaryDto> getPiecesSummariesForVehicle(Integer vehicleId) {
        var query = entityManager.createQuery("""
          SELECT DISTINCT p
          FROM VehiclePieceOperationAssociationEntity vpoa
          JOIN vpoa.pieceOperation po
          JOIN po.piece p
          WHERE vpoa.vehicle.id = :vehicleId
          """, PieceEntity.class);
        query.setParameter("vehicleId", vehicleId);
        var pieces = query.getResultList();
        return pieces.stream().map(pieceMapper::fromEntityToDto).toList();
    }

    public boolean piecesNotAssociatedToVehicle(List<String> piecesXids, Integer vehicleId) {
        // avoids a useless query if the list is empty
        if (piecesXids.isEmpty())
            return false;

        return !new HashSet<>(
                getPiecesSummariesForVehicle(vehicleId)
                        .stream()
                        .map(PieceSummaryDto::getXid)
                        .toList()
        ).containsAll(piecesXids);
    }
}
