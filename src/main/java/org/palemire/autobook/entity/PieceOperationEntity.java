package org.palemire.autobook.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "piece_operation")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "category")
@Getter
@Setter
public abstract class PieceOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    /**
     * CascadeType.PERSIST because we *may* attach a new PieceEntity to persist based on the API endpoint.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "piece_id")
    private PieceEntity piece;

    /**
     * CascadeType.DELETE is already managed by the DB Engine.
     */
    @OneToMany(mappedBy = "pieceOperation", cascade =   CascadeType.PERSIST)
    private Set<VehiclePieceOperationAssociationEntity> vehicleAssociations = new HashSet<>();

}
