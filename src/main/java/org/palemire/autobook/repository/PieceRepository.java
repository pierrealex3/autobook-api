package org.palemire.autobook.repository;

import org.palemire.autobook.entity.PieceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PieceRepository extends JpaRepository<PieceEntity, Long> {
    @Query("select p.id from PieceEntity p where p.xid = :pieceXid")
    Optional<Long> findIdByXid(String pieceXid);
}
