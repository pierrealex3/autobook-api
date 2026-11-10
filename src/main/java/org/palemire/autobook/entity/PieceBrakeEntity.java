package org.palemire.autobook.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "piece_brake")
@DiscriminatorValue("BRAKE")
public class PieceBrakeEntity extends PieceEntity {
}
