package org.palemire.autobook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "piece_operation_vehicle_link")
@Getter
@Setter
public class VehiclePieceOperationAssociationEntity {

    @Id
    @Column(name = "piece_operation_id")
    private Integer pieceOperationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_operation_id")
    private PieceOperationEntity pieceOperation;

    @Column(name = "odometer_km")
    private Integer odometerKm;
}
