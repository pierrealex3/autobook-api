package org.palemire.autobook.appointment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "piece_wiper")
@DiscriminatorValue("WIPER")
public class PieceWiperEntity extends PieceEntity {
}
