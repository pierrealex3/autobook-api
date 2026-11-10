package org.palemire.autobook.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicle_piece_operation_asso")
@Getter
@Setter
public class VehiclePieceOperationAssociationEntity {

    @EmbeddedId
    private VehiclePieceOperationAssociationId id = new VehiclePieceOperationAssociationId();

    @MapsId("vehicleId")
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @MapsId("pieceOperationId")
    @ManyToOne
    @JoinColumn(name = "piece_operation_id")
    private PieceOperationEntity pieceOperation;

    @Column(name = "odometer_km")
    private Integer odometerKm;
}
