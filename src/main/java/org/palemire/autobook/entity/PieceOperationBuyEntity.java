package org.palemire.autobook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;

@Entity
@Table(name = "piece_operation_buy")
@DiscriminatorValue("BUY")
public class PieceOperationBuyEntity extends PieceOperationEntity {
}
