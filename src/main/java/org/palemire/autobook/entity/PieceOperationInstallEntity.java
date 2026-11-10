package org.palemire.autobook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;

@Entity
@Table(name = "piece_operation_install")
@DiscriminatorValue("INSTALL")
public class PieceOperationInstallEntity extends PieceOperationEntity {
}
