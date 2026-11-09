package org.palemire.autobook.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class VehiclePieceOperationAssociationId implements Serializable {

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "piece_operation_id")
    private Integer pieceOperationId;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof VehiclePieceOperationAssociationId that)) return false;
        return Objects.equals(vehicleId, that.vehicleId) && Objects.equals(pieceOperationId, that.pieceOperationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, pieceOperationId);
    }
}
