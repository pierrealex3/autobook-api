package org.palemire.autobook.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "piece_tire")
@Entity
@DiscriminatorValue("TIRE")
@Getter
@Setter
public class PieceTireEntity extends  PieceEntity {

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @OneToOne
    @JoinColumn(name = "tire_season_id")
    private StaticTireSeasonEntity tireSeason;

}
