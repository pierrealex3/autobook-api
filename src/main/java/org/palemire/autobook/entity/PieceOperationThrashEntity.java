package org.palemire.autobook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;

@Entity
@Table(name = "piece_operation_thrash")
@DiscriminatorValue("THRASH")
public class PieceOperationThrashEntity extends PieceOperationEntity {
}
