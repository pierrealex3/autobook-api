package org.palemire.autobook.repository;

import org.palemire.autobook.entity.PieceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PieceRepository extends JpaRepository<PieceEntity, Long> {
}
